import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.ReadExcelFile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PersonalInformationTest {
    private static WebDriver driver;
    private static String URL = "https://www.takealot.com/account/login?returnTo=https%3A%2F%2Fwww.takealot.com%2Faccount";

    @BeforeClass
    public void setUpDriver(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test(dataProvider = "informationData")
    public void testInformation(String email, String password, String firstName, String lastName) throws InterruptedException {
        driver.get(URL);

        Thread.sleep(5000);

        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("button.button.submit-action[data-ref='dynaform-submit-button']")).click();
        Thread.sleep(5000);

        driver.get("https://www.takealot.com/account/personal-details");

        Thread.sleep(5000);


        WebElement nameButtonBanner = driver.findElement(By.cssSelector("div.button-banner[data-ref='Your Name-button']"));

        Thread.sleep(3000);
        nameButtonBanner.findElement(By.cssSelector("div.button.ghost.blue")).click();
        Thread.sleep(2000);

        WebElement firstNameEdit = driver.findElement(By.id("name_firstName"));

        firstNameEdit.clear();

        firstNameEdit.sendKeys(firstName);
        Thread.sleep(3000);

        WebElement lastNameEdit = driver.findElement(By.id("name_lastName"));
        Thread.sleep(3000);
        lastNameEdit.clear();
        Thread.sleep(3000);
        lastNameEdit.sendKeys(lastName);

        Thread.sleep(5000);

        driver.findElement(By.cssSelector("button.submit-button")).click();

        Thread.sleep(3000);

        WebElement nameItem = driver.findElement(By.cssSelector("li[data-ref='name-item']"));
        Thread.sleep(3000);
        Assert.assertTrue(nameItem.getText().contains(firstName));


    }

    @DataProvider(name = "informationData")
    public Object[][] testDataFeed(){
        int sheetNum = 4;
        ReadExcelFile config = new ReadExcelFile("takeAlotSheet.xlsx");

        int rows = config.getRowCount(sheetNum);
        Object[][] personalData = new Object[rows][4];

        for (int i = 0; i < rows; i++) {
            personalData[i][0] = config.getData(sheetNum,i,0);
            personalData[i][1] = config.getData(sheetNum,i,1);
            personalData[i][2] = config.getData(sheetNum,i,2);
            personalData[i][3] = config.getData(sheetNum,i,3);
        }
        return personalData;
    }

    @AfterClass
    public void cleanup(){
        driver.quit();
    }
}
