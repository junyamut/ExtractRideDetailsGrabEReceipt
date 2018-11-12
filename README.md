# Extract Ride Details from Grab E-receipt
#### (terminal application only)

*Description: Extracts all the important booking details / receipt summary of a Grab ride-receipt.*

## Intro

To be used in conjunction with [Gmail Find and Extract Messages](https://github.com/junyamut/GmailFindAndExtractMessages). Requires the latter has retrieved and saved local copies of Grab e-receipts from your Gmail account.  

#### Requirements
* Java 1.7 or greater
* Gradle 2.3 or greater *(Or you use Maven)*
* Downloaded HTML files of Grab e-receipts

#### Steps To Run
1. Make sure you already ran GmailFindAndExtractMessages app and results are already downloaded.
2. Copy the HTML files from #1 to the *receipts* directory relative to the root of the project directory.
2. Open a terminal. 
3. Change to the appropriate directory.
4. Run with command 'gradle -q run'.

### Note
Right now the source directory for the Grab e-receipts is assumed to be named as 'receipts', which is located relative to the root of the project directory.

The output directory is named as 'workbooks'. This is where the spreadsheet will be saved.

Spreadsheet name is 'My Grab Rides.xlsx'.

All the above may be found under the main class *(ExtractRideDetailsGrabEReceipt)*.  You may change this as necessary.

#### Gradle Setup
```gradle
repositories {
	mavenCentral();
}

dependencies {
	compile 'org.jsoup:jsoup:1.11.3'
	compile 'org.apache.poi:poi:3.17'
	compile 'org.apache.poi:poi-ooxml:3.17'
}
```
#### Caveats
* Rides are not sorted properly according to date when written into the spreadsheet.
* Will not check if e-receipt data is already in the spreadsheet. As a result, you might end up with duplicate entries.
* Does not read symbolic link files. Make sure you copy the e-receipt HTML files to the 'receipts' directory.

#### Useful References
* [Gmail Find and Extract Messages](https://github.com/junyamut/GmailFindAndExtractMessages)

#### Todo
1. Sort the entries properly according to date. 
2. Determine if e-receipt data is already in the spreadsheet through Booking Code and skip from writing. 
3. Set Data boundary - limit how many lines of data per sheet, then create a new one.
4. Create a settings file and put all the arbitrary values (e.g. workbook name) in there instead of changing it directly in the class.