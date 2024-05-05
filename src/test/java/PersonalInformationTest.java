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

        driver.findElement(By.cssSelector(".button.ghost.blue[data-ref='Your Name-button']")).click();

        WebElement firstNameEdit = driver.findElement(By.id("name_lastName"));
        firstNameEdit.clear();
        firstNameEdit.sendKeys(firstName);

        WebElement lastNameEdit = driver.findElement(By.id("name_lastName"));
        lastNameEdit.clear();
        lastNameEdit.sendKeys(lastName);

        Thread.sleep(5000);

        driver.findElement(By.cssSelector(".button.submit-button")).click();

        WebElement nameItem = driver.findElement(By.cssSelector("li[data-ref='name-item']"));
        Assert.assertTrue(nameItem.getText().contains(firstName));


    }

    @DataProvider(name = "informationData")
    public Object[][] testDataFeed(){
        int sheetNum = 3;
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
