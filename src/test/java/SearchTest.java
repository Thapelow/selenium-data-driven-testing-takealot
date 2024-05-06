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

import java.awt.*;
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

        driver.findElement(By.cssSelector("input[name='search']")).sendKeys(item + " " + brand + " " + color, Keys.ENTER);

        Thread.sleep(5000);

        List<WebElement> itemElements = driver.findElements(By.cssSelector("a.product-anchor"));
        List<String> actualItems = itemElements.stream()
                .map(element -> element.getText().toLowerCase() + element.getAttribute("href").toLowerCase())
                .toList();

        List<String> expectedItems = actualItems.stream()
                .filter(el -> el.contains(item.toLowerCase()) && el.contains(brand.toLowerCase()) && el.contains(color.toLowerCase()))
                .toList();

        Assert.assertFalse(expectedItems.isEmpty(), "No items found matching the specified criteria: " + item + ", " + brand + ", " + color);
    }



    @DataProvider(name = "searchData")
    private Object[][] testDataFeed(){
        int sheetNum = 2;
        ReadExcelFile config = new ReadExcelFile("takeAlotSheet.xlsx");

        int rows = config.getRowCount(sheetNum);
        Object[][] regData = new Object[rows][3];

        for (int i = 0; i < rows; i++) {
            regData[i][0] = config.getData(sheetNum,i,0);
            regData[i][1] = config.getData(sheetNum,i,1);
            regData[i][2] = config.getData(sheetNum,i,2);
        }

        return regData;
    }

    @AfterClass
    public void cleanup(){
        driver.quit();
    }
}
