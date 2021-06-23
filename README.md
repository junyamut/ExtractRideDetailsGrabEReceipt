# Extract Ride Details from Grab E-receipt
#### (terminal application only)

### Note: _This will probably not work as is anymore since the HTML formatting for email receipts from Grab has already changed_

*Description: Extracts all the important booking details / receipt summary of a Grab ride-receipt.*

## Intro

To be used in conjunction with [Gmail Find and Extract Messages](https://github.com/junyamut/GmailFindAndExtractMessages). Requires the latter has retrieved and saved local copies of Grab e-receipts from your Gmail account.

This app will extract the ride data, as shown in the image below, from a Grab e-receipt HTML file, and saves all individual values into a spreadsheet. Appends new ride data if spreadsheet already exists.

![Grab E-receipt](https://github.com/junyamut/ExtractRideDetailsGrabEReceipt/blob/master/samples/grab-e-receipt.png)

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

*Theoretically, if you are able to download and save the emailed Grab e-receipt mail's body part in HTML (which should be a fully-qualified HTML document as provided as is by Grab) from another mail provider (not Gmail) this app should still work.*

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
5. Export the spreadsheet to Google Sheets.

#### DISCLAIMER
*Grab e-receipts ARE NOT being retrieved directly from the Grab app or website - where possible - or through any other means that goes into Grab's servers/networks. These are instead taken from the Grab account owner's e-receipts from their Gmail account which is being emailed by Grab after each ride is completed (assumed that the Grab account has Gmail account in their user profile as per the author of this work). Thus the companion app Gmail Find and Extract Messages was created.*
