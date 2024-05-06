import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.ReadExcelFile;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class AddToCartTest {

    private static WebDriver driver;
    private static String URL = "https://www.takealot.com/account/login?returnTo=https%3A%2F%2Fwww.takealot.com%2Faccount";

    @BeforeClass
    public static void setUpDriver(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test(dataProvider = "cartData")
    public void testAddToCart(String email, String password,String item, String brand, String color) throws InterruptedException {
        driver.get(URL);

        Thread.sleep(5000);

        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(password);
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("button.button.submit-action[data-ref='dynaform-submit-button']")).click();
        Thread.sleep(5000);

        driver.findElement(By.cssSelector("input[name='search']")).sendKeys(item + " " + brand + " " + color, Keys.ENTER);

        Thread.sleep(5000);

        List<WebElement> addToCartIcons = driver.findElements(By.cssSelector("button.add-to-cart-button-module_add-to-cart-button_1a9gT"));


        if (!addToCartIcons.isEmpty()){
            addToCartIcons.get(0).click();
        }

        driver.get("https://www.takealot.com/cart");
        Thread.sleep(5000);
        List<WebElement> cartItems = driver.findElements(By.cssSelector("div.cart-item-container-module_item_3Vkqc"));
        Thread.sleep(5000);

        Assert.assertNotNull(cartItems, "Cart item element not found");

    }

    @DataProvider(name = "cartData")
    private Object[][] testDataFeed(){
        int sheetNum = 3;
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

        return regData;
    }

    @AfterClass
    public void cleanup(){
        driver.quit();
    }
}
