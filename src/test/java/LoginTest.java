import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.ReadExcelFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest {
    private static WebDriver driver;
    private static String URL = "https://www.takealot.com/account/login?returnTo=https%3A%2F%2Fwww.takealot.com%2Faccount";

    @BeforeClass
    public static void setUpDriver(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String email, String password) throws InterruptedException {
        driver.get(URL);

        Thread.sleep(5000);

        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("button.button.submit-action[data-ref='dynaform-submit-button']")).click();
        Thread.sleep(5000);

        driver.get("https://www.takealot.com/account/personal-details");

        Assert.assertEquals(driver.getCurrentUrl(), "https://www.takealot.com/account/personal-details", "Failed to login");
    }

    @DataProvider(name = "loginData")
    private Object[][] TestDataFeed(){
        int sheetNum = 1;
        ReadExcelFile config = new ReadExcelFile("takeAlotSheet.xlsx");

        int rows = config.getRowCount(sheetNum);
        Object[][] regData = new Object[rows][2];

        for (int i = 0; i < rows; i++) {
            regData[i][0] = config.getData(sheetNum,i,0);
            regData[i][1] = config.getData(sheetNum,i,1);
        }

        return regData;
    }

    @AfterClass
    public void cleanup(){
        driver.quit();
    }
}
