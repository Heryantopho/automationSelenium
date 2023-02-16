package superInterviewTest;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class webTesting {
	
	static WebDriver driver = new ChromeDriver();
	static JavascriptExecutor js = (JavascriptExecutor) driver;

	public static void main(String[] args) throws Exception {
		//open browser method
		openBrowser("https://www.saucedemo.com/");
		//login method, parameternya username dan password
		Thread.sleep(3000);
		loginAndAssertHomePage("standard_user", "secret_sauce");
		Thread.sleep(3000);
		//sort product to highest price method
		sortTheProductandAssert();
		Thread.sleep(3000);
		//click and assert pdp method, parameternya index product yang mau dipilih
		clickAndAssertPDP(2);
		Thread.sleep(3000);
		//buy product method
		buyProduct();
		Thread.sleep(3000);
		//input checkout form method, parameternya firstName, lastName dan postalCode
		inputDetailsCheckout("Rinda", "Angeline", "12345");
		Thread.sleep(3000);
		//screenshot  method, parameternya lokasi foto hasil screenshot
		takeSnapShot(driver, "D://screenshotSauceDemoCheckout.png");
	}
	
	static void openBrowser(String urlAddress){
		System.setProperty("webdriver.chrome.driver", "C://chromedriver.exe");
		driver.get(urlAddress);
		driver.manage().window().maximize();
	}
	
	static void loginAndAssertHomePage(String username, String password){
		driver.findElement(By.xpath("//*[@data-test='username']")).sendKeys(username);
		driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
		
		driver.findElement(By.xpath("//*[@data-test='password']")).sendKeys(password);
		driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
		driver.findElement(By.xpath("//*[@data-test='login-button']")).click();
	
		//assert
		driver.findElement(By.xpath("//*[@class='inventory_item_name']")).isDisplayed();
		driver.findElement(By.xpath("//*[@class='inventory_item_desc']")).isDisplayed();
		driver.findElement(By.xpath("//*[@class='inventory_item_price']")).isDisplayed();
		driver.findElement(By.xpath("//*[@data-test='add-to-cart-sauce-labs-backpack']")).isDisplayed();
	}
	
	static void sortTheProductandAssert() throws InterruptedException {
		driver.findElement(By.xpath("//*[@data-test='product_sort_container']")).click();
		driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
		
		driver.findElement(By.xpath("//*[@value='hilo']")).click();
		driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
		
		String sPrice = driver.findElement(By.xpath("(//*[@class='inventory_item_price'])[1]")).getText();
		sPrice = sPrice.replace("$", "");

		String sPrice2 = driver.findElement(By.xpath("(//*[@class='inventory_item_price'])[2]")).getText();
		sPrice2 = sPrice2.replace("$", "");
		
		double iPrice = Double.parseDouble(sPrice);
		double iPrice2 = Double.parseDouble(sPrice2);
		
		assert iPrice > iPrice2;
	}
	
	static void clickAndAssertPDP(int indexItem){
		
	    String xpathItemName = String.format("(//*[@class='inventory_item_name'])[%d]", indexItem);
	    String xpathItemPrice = String.format("(//*[@class='inventory_item_price'])[%d]", indexItem);
		
		String productListName = driver.findElement(By.xpath(xpathItemName)).getText();
		String productListPrice =  driver.findElement(By.xpath(xpathItemPrice)).getText();
		
		driver.findElement(By.xpath(xpathItemName)).click();
		
		String pdpName = driver.findElement(By.xpath("//*[@class='inventory_details_name large_size']")).getText();
		String pdpPrice = driver.findElement(By.xpath("//*[@class='inventory_details_price']")).getText();
		
		assert productListName == pdpName;
		assert productListPrice == pdpPrice;
	}
	
	static void buyProduct() {
		driver.findElement(By.xpath("//*[@class='btn btn_primary btn_small btn_inventory']")).click();
		driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
		
		driver.findElement(By.xpath("//*[@id='shopping_cart_container']")).click();
		driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);

		
		driver.findElement(By.xpath("//*[@data-test='checkout']")).click();
		driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);

		driver.findElement(By.xpath("//*[@id='checkout_info_container']")).isDisplayed();
		
	}
	
	static void inputDetailsCheckout(String firstName, String lastName, String postalCode) {
		driver.findElement(By.xpath("//*[@data-test='firstName']")).sendKeys(firstName);
		driver.findElement(By.xpath("//*[@data-test='lastName']")).sendKeys(lastName);
		driver.findElement(By.xpath("//*[@data-test='postalCode']")).sendKeys(postalCode);
		driver.findElement(By.xpath("//*[@data-test='continue']")).click();
		
		driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);

		WebElement btnFinish = driver.findElement(By.xpath("//*[@data-test='finish']"));
		js.executeScript("arguments[0].scrollIntoView();", btnFinish);
		
		driver.manage().timeouts().implicitlyWait(10000, TimeUnit.MILLISECONDS);
		driver.findElement(By.xpath("//*[@data-test='finish']")).click();
		
		//scroll keatas buat screenshot
		WebElement btnCart = driver.findElement(By.xpath("//*[@id='shopping_cart_container']"));
		js.executeScript("arguments[0].scrollIntoView();", btnCart);
		
	}
	
	static void takeSnapShot(WebDriver webdriver,String fileWithPath) throws Exception{
		
	        TakesScreenshot scrShot =((TakesScreenshot)webdriver);

	                File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
	                
	                FileUtils.copyFile(SrcFile, new File(fileWithPath));  
	    }
}
