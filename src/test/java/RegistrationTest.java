import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.ReadExcelFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

public class RegistrationTest {
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static final String URL = "http://takealot.com";

    @BeforeMethod
    public static void setUpDriver(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test(dataProvider = "registrationData")
    public void registerNewUser(String firstName, String lastName, String email, String password, String phoneNumber){
        driver.get(URL);

        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        driver.findElement(By.linkText("Register")).click();
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));

        driver.findElement(By.name("first_name")).sendKeys(firstName);
        driver.findElement(By.name("last_name")).sendKeys(lastName);
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("new_password")).sendKeys(password);
        driver.findElement(By.name("mobile_country_code")).sendKeys(phoneNumber);
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        WebElement verifyElement = driver.findElement(By.className("cell auto"));

        Assert.assertTrue(verifyElement.isDisplayed());
    }


    @DataProvider(name = "registrationData")
    public Object[][] TestDataFeed(){

        ReadExcelFile config = new ReadExcelFile("takeAlotSheet.xlsx");
        int rows = config.getRowCount(0);

        Object[][] regData = new Object[rows][5];

        for (int i = 0; i < rows; i++) {
            regData[i][0] = config.getData(0,i,0);
            regData[i][1] = config.getData(0,i,1);
            regData[i][2] = config.getData(0,i,2);
            regData[i][3] = config.getData(0,i,3);
            regData[i][4] = config.getData(0,i,4);
        }
        return  regData;
    }

    @AfterMethod
    public void cleanup(){
        driver.quit();
    }
}
