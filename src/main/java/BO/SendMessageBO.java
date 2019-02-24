package BO;

import POM.GmailHomePage;

import models.Letter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SendMessageBO {

    private GmailHomePage gmailHomePage;

    private static Logger logger = LogManager.getLogger(SendMessageBO.class);

    private WebDriver driver;
    private WebDriverWait wait;
    private Letter letter;          //contains letter fields

    public SendMessageBO (WebDriver driver, Letter letter){
        gmailHomePage = new GmailHomePage(driver);
        this.driver = driver;
        this.letter = letter;
        this.wait = new WebDriverWait(driver,10);
    }

    public void writeMessageAndClose (){
        writeLetter(letter);
        gmailHomePage.saveMessage();
        wait.until(ExpectedConditions.invisibilityOf(gmailHomePage.getMessageTextarea()));  //waiting for closing letter form
        logger.info("Message has been closed");
    }

    public void writeLetter (Letter letter){
        gmailHomePage.clickCompose();
        wait.until(ExpectedConditions.visibilityOf(gmailHomePage.getSaveAndCloseButton()));  //waiting for opening letter form
        gmailHomePage.writeLetter(letter);
        logger.info("Message has been written");
    }

    public void openDraftMessage(){
        gmailHomePage.openDraftFolder();
        logger.info("Draft folder has been opened");
        gmailHomePage.openDraftMessage();
        wait.until(ExpectedConditions.elementToBeClickable(gmailHomePage.getSendButton()));  //waiting for opening letter form
    }

    public boolean isValidateFields(Letter letter){
        if(gmailHomePage.getLetter().checkFields(letter)) {
            logger.info("All fields are valid");
            return true;
        }
        else{
            logger.error("Test failed");
            return false;
        }
    }

    public boolean isSent(){
        try {
            wait.until(ExpectedConditions.visibilityOf(gmailHomePage.getIsSentLabel()));
        }
        catch (Exception ex){
            logger.error("Test failed");
            return false;
        }
        return true;
    }

    public void sendDraftLetter(){
        gmailHomePage.sendMessage();
        logger.info("Message has been sent");
    }
}
