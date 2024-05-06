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
import java.util.stream.Collectors;

public class SearchTest {

    private static WebDriver driver;
    private static String URL = "http://www.takealot.com";

    @BeforeClass
    public static void setUpDriver(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test(dataProvider = "searchData")
    public void testSearch(String item, String brand, String color) throws InterruptedException {
        driver.get(URL);

        Thread.sleep(5000);

        driver.findElement(By.cssSelector("input[name='search']")).sendKeys(item,Keys.ENTER);

        Thread.sleep(5000);

        driver.findElement(By.cssSelector("label[for='filter_"+brand+"_" + brand + "']")).click();

        Thread.sleep(3000);

        driver.findElement(By.xpath("//span[text()='" + color + "]")).click();

        Thread.sleep(5000);

        List<WebElement> itemElements = driver.findElements(By.cssSelector("a.product-anchor"));
        List<String> actualItems = itemElements.stream()
                .map(element -> element.getText().toLowerCase() + element.getAttribute("href").toLowerCase())
                .collect(Collectors.toList());


        List<String> expectedItems = actualItems.stream()
                .filter(el -> el.contains(item.toLowerCase()) && el.contains(brand.toLowerCase()) && el.contains(color.toLowerCase()))
                .collect(Collectors.toList());

        Assert.assertEquals(expectedItems, actualItems);
        addToCart();

    }

    @Test
    public void addToCart(){
        List<WebElement> addToCartIcons = driver.findElements(By.cssSelector("button.add-to-cart-button"));

        if (!addToCartIcons.isEmpty()){
            addToCartIcons.get(0).click();
        }

        driver.get("https://www.takealot.com/cart");
        List<WebElement> cartItems = driver.findElements(By.cssSelector("div.cart-item-container-module_item_3Vkqc"));

        Assert.assertNotNull(cartItems, "Cart item element not found");
    }



    @DataProvider(name = "searchData")
    private Object[][] testDataFeed(){
        ReadExcelFile config = new ReadExcelFile("takeAlotSheet.xlsx");

        int rows = config.getRowCount(2);
        Object[][] regData = new Object[rows][2];

        for (int i = 0; i < rows; i++) {
            regData[i][0] = config.getData(2,i,0);
            regData[i][1] = config.getData(2,i,1);
            regData[i][2] = config.getData(2,i,2);
        }

        return regData;
    }

    @AfterClass
    public void cleanup(){
        driver.quit();
    }
}
