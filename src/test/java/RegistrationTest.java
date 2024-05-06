import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.ReadExcelFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class RegistrationTest {
    private static WebDriver driver;
    private static final String URL = "https://www.takealot.com/account/register?returnTo=https%3A%2F%2Fwww.takealot.com%2Faccount";

    @BeforeClass
    public static void setUpDriver(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test(dataProvider = "registrationData")
    public void registerNewUser(String firstName, String lastName, String email, String password, String phoneNumber) throws InterruptedException {
        driver.get(URL);

        Thread.sleep(5000);
        driver.findElement(By.name("first_name")).sendKeys(firstName);
        driver.findElement(By.name("last_name")).sendKeys(lastName);
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("new_password")).sendKeys(password);
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("input[name='mobile_national_number']")).sendKeys(phoneNumber);
        Thread.sleep(5000);
        WebElement continueButton = driver.findElement(By.cssSelector("button.button.submit-action[data-ref='dynaform-submit-button']"));
        continueButton.click();


        Thread.sleep(5000);


        WebElement closeButton = driver.findElement(By.cssSelector("button.modal-module_close-button_asjao[data-ref='modal-close-button']"));

        Assert.assertTrue(closeButton.isDisplayed(), "Close button element is displayed after registering.");
        driver.get("http://takealot.com");
        Thread.sleep(5000);

    }

    @DataProvider(name = "registrationData")
    private Object[][] TestDataFeed(){
        int sheetNum = 0;
        ReadExcelFile config = new ReadExcelFile("takeAlotSheet.xlsx");
        int rows = config.getRowCount(sheetNum);

        Object[][] regData = new Object[rows][5];

        for (int i = 0; i < rows; i++) {
            regData[i][0] = config.getData(sheetNum,i,0);
            regData[i][1] = config.getData(sheetNum,i,1);
            regData[i][2] = config.getData(sheetNum,i,2);
            regData[i][3] = config.getData(sheetNum,i,3);
            regData[i][4] = config.getData(sheetNum,i,4);
        }
        return  regData;
    }

    @AfterClass
    public void cleanup(){
        driver.quit();
    }
}
