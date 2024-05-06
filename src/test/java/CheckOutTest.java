import io.github.bonigarcia.wdm.WebDriverManager;
import org.example.ReadExcelFile;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

public class CheckOutTest {
    private WebDriver driver;
    private String URL = "";

    @BeforeClass
    public void setUpDriver(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test(dataProvider = "checkOutData")
    public void testCheckOut(String email, String password, String item, String brand, String color, String name, String mobile,
                             String street, String suburb, String town, String province, String code) throws InterruptedException {

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

        Thread.sleep(3000);

        driver.findElement(By.xpath("//*[@id=\"shopfront-app\"]/div[4]/div[2]/section/div[2]/div[2]/div/div[1]/div[3]/div/div[2]/a")).click();

        Thread.sleep(3000);

        driver.findElement(By.xpath("//*[@id=\"Delivery_button\"]/div/div[2]/div")).click();

        Thread.sleep(3000);

        driver.findElement(By.xpath("//*[@id=\"shopfront-app\"]/div[2]/div/section[1]/div/div/div[2]/div[1]/div/div/div[1]/div/div/label")).click();

        driver.findElement(By.xpath("//*[@id=\"address-edit_recipient\"]")).sendKeys(name);
        driver.findElement(By.xpath("//*[@id=\"address-edit_contact_number\"]")).sendKeys(mobile);
        driver.findElement(By.xpath("//*[@id=\"address-edit_street\"]")).sendKeys(street);
        driver.findElement(By.xpath("//*[@id=\"address-edit_suburb\"]")).sendKeys(suburb);
        driver.findElement(By.xpath("//*[@id=\"address-edit_city\"]")).sendKeys(town);

        List<WebElement> provinceOptions = driver.findElements(By.cssSelector("ul.select-drawer-module_list_29ob6 li button.select-drawer-option"));

        for (WebElement option : provinceOptions) {
            if (option.getText().equals(province)) {
                option.click();
                break;
            }
        }

        driver.findElement(By.xpath("//*[@id=\"address-edit_postal_code\"]")).sendKeys(code);
        driver.findElement(By.xpath("//*[@id=\"shopfront-app\"]/div[1]/div/section[1]/div[2]/div/div/div[11]/div/button")).click();

        Thread.sleep(3000);

        driver.findElement(By.xpath("//*[@id=\"shopfront-app\"]/div[2]/div/section[1]/div/div[3]/div/button[2]")).click();

        Thread.sleep(3000);

        driver.findElement(By.xpath("//*[@id=\"gmap_address-autocomplete\"]")).sendKeys(street+", "+suburb+", "+town);
        Thread.sleep(3000);

        driver.findElement(By.xpath("//*[@id=\"shopfront-app\"]/div[2]/div/section[1]/section/div[4]/div/button[2]")).click();

        Thread.sleep(3000);

        driver.findElement(By.xpath("//*[@id=\"shopfront-app\"]/div[2]/div/section[2]/aside/section/div[4]/div/div/div/div/button")).click();
        Thread.sleep(3000);
        driver.findElement(By.xpath("//*[@id=\"shopfront-app\"]/div[2]/div/section[2]/aside/section/div[4]/div/div/div/div/button")).click();

        Thread.sleep(5000);

    }

    @DataProvider(name = "checkOutData")
    private Object[][] testDataFeed(){
        int sheetNum = 5;

        ReadExcelFile config = new ReadExcelFile("");
        int rows = config.getRowCount(sheetNum);

        Object[][] regData = new Object[rows][11];

        for (int i = 0; i < rows; i++) {
            regData[i][0] = config.getData(sheetNum,i,0);
            regData[i][1] = config.getData(sheetNum,i,1);
            regData[i][2] = config.getData(sheetNum,i,2);
            regData[i][3] = config.getData(sheetNum,i,3);
            regData[i][4] = config.getData(sheetNum,i,4);
            regData[i][5] = config.getData(sheetNum,i,5);
            regData[i][6] = config.getData(sheetNum,i,6);
            regData[i][7] = config.getData(sheetNum,i,7);
            regData[i][8] = config.getData(sheetNum,i,8);
            regData[i][9] = config.getData(sheetNum,i,9);
            regData[i][10] = config.getData(sheetNum,i,10);
        }
        return regData;
    }

    @AfterClass
    public void cleanup(){
        driver.quit();
    }
}
