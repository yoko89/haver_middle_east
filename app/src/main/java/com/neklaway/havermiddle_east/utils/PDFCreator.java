package com.neklaway.havermiddle_east.utils;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;

import androidx.lifecycle.MutableLiveData;

import com.neklaway.havermiddle_east.R;
import com.neklaway.havermiddle_east.entities.Customer;
import com.neklaway.havermiddle_east.entities.HMECode;
import com.neklaway.havermiddle_east.entities.TimeSheet;
import com.neklaway.havermiddle_east.roomDataBase.repository.HMECODERepository;
import com.neklaway.havermiddle_east.roomDataBase.repository.TimeSheetRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class PDFCreator {

    public final MutableLiveData<Boolean> pdfCreated;

    final List<TimeSheet> timeSheetList;
    final HMECode hmeCode;
    final Customer customerData;
    boolean isIbauUser;
    final String signerName;
    final Application application;

    private final TimeSheetRepository timeSheetRepository;
    private final HMECODERepository hmecodeRepository;


    //Times new roman font
    final Typeface times;
    final Typeface timesBold;

    //--- Paints  ---

    //Time Sheet Header
    final Paint paintHeader = new Paint();

    // Normal Text
    final Paint paintText = new Paint();

    // Bold Text
    final Paint paintBoldText = new Paint();

    // Thick blue for border
    final Paint paintThickBlue = new Paint();

    // Blue for Lines
    final Paint paintBlue = new Paint();

    // Thick blue for Table outline
    final Paint paintThick = new Paint();

    // Paint to Fill pdf Data
    final Paint paintData = new Paint();

    // Bitmap for Signatures
    final BitmapFactory.Options options = new BitmapFactory.Options();

    //create PDF
    final PdfDocument pdfDocument = new PdfDocument();

    //Create page description
    final ArrayList<PdfDocument.PageInfo> pageInfo = new ArrayList<>();

    // start a page
    final ArrayList<PdfDocument.Page> page = new ArrayList<>();

    final ArrayList<Canvas> canvas = new ArrayList<>();

    // Shared preferences
    static final String Settings = "user_settings";
    static final String UserName = "user_name_key";
    static final String IBAUUser = "IBAU_user_key";

    //Table Header Shift Constants
    static final int Y_TABLE_TOP = 210;
    static final int X_TABLE_LEFT = 15;
    static final int TABLE_WIDTH = 560;
    static final int COLUMN_SHIFT = 51;
    static final int COLUMN_SHIFT_DATE = COLUMN_SHIFT - 12;

    //Shift Constants
    final float textRowHeight = paintText.descent() - paintText.ascent();

    //Y position start initialization
    final float Y_POSITION_START = Y_TABLE_TOP + 2 * (textRowHeight);
    float yPositionForCurrentItem = Y_POSITION_START;

    //HME Logo and data
    static final int LOGO_X_START = 420;
    static final int LOGO_X_END = 580;
    static final int LOGO_Y_START = 10;
    static final int LOGO_Y_END = 83;
    static final int DATA_Y = LOGO_Y_END + 2;

    //Data part
    static final int LEFT_DATA_START = 10;
    static final int RIGHT_DATA_START = 220;
    static final int DATA_TOP = 50;

    //Fill Data Shifts
    static final int SERVICE_ENGINEER_SHIFT = 82;
    static final int CUSTOMER_SHIFT = 55;
    static final int MACHINE_TYPE_SHIFT = 70;
    static final int DEPARTURE_SHIFT = 55;
    static final int HME_CODE_SHIFT = 95;
    static final int WORK_DESCRIPTION_SHIFT = 95;
    static final int MACHINE_NUMBER_SHIFT = 80;
    static final int ARRIVAL_SHIFT = 45;
    static final int CITY_SHIFT = 60;
    static final int COUNTRY_SHIFT = 80;
    static final int IBAU_SO_SHIFT = 100;

    // PDF Tables Shifts
    static final int DAY_HEADER_SHIFT = 5;
    static final int DAY_SHIFT = 5;
    static final int DATE_HEADER_SHIFT = 5;
    static final int DATE_SHIFT = 2;
    static final int TRAVEL_START_HEADER_SHIFT = 5;
    static final int TRAVEL_START_SHIFT = 5;
    static final int WORK_START_HEADER_SHIFT = 5;
    static final int WORK_START_SHIFT = 5;
    static final int WORK_END_HEADER_SHIFT = 5;
    static final int WORK_END_SHIFT = 5;
    static final int TRAVEL_END_HEADER_SHIFT = 5;
    static final int TRAVEL_END_SHIFT = 5;
    static final int BREAK_HEADER_SHIFT = 5;
    static final int BREAK_SHIFT = 5;
    static final int WORKING_HOURS_HEADER_SHIFT = 5;
    static final int WORKING_HOURS_SHIFT = 5;
    static final int OVER_TIME_HEADER_SHIFT = 2;
    static final int OVER_TIME_SHIFT = 2;
    static final int TRAVEL_HOURS_HEADER_SHIFT = 5;
    static final int TRAVEL_HOURS_SHIFT = 5;
    static final int TRAVEL_DISTANCE_HEADER_SHIFT = 5;
    static final int TRAVEL_DISTANCE_SHIFT = 5;
    static final int TOTAL_SHIFT = 10;


    //Create Signature and date
    static final int CHECKED_SHIFT = 35;
    static final int CUSTOMER_SIGN_SHIFT = 250;
    static final int CUSTOMER_SIGNATURE_IMG_SHIFT = CUSTOMER_SIGN_SHIFT - 50;
    static final int CUSTOMER_SIGNATURE_DATE_SHIFT = CUSTOMER_SIGN_SHIFT;
    static final int ENGINEER_SIGN_SHIFT = 480;
    static final int ENGINEER_SIGNATURE_IMG_SHIFT = ENGINEER_SIGN_SHIFT - 20;
    static final int ENGINEER_SIGNATURE_DATE_SHIFT = ENGINEER_SIGN_SHIFT + 30;
    static final int ENGINEER_SIGN_WIDTH = 150;
    static final int CUSTOMER_SIGN_WIDTH = 100;
    static final int SIGNATURE_BOTTOM = 815;
    static final int SIGNATURE_LINE_SHIFT = -5;
    static final int CHECKED_LINE_LENGTH = 70;
    static final int CUSTOMER_LINE_LENGTH = 90;
    static final int ENGINEER_LINE_LENGTH = 90;

    // Date Formatter
    final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault());

    // Global variables
    int userSignHeight = 100;
    int customerSignHeight = 100;

    //Counters
    private float totalWorkMinutes = 0;
    private float totalTravelMinutes = 0;
    private float totalOverTime = 0;
    int totalTravelDistance = 0;

    final File userSign;
    final File customerSign;

    public PDFCreator(List<TimeSheet> timeSheetList, HMECode hmeCode, Customer customerData, boolean isIbauUser, String signerName, Application application) {
        this.timeSheetList = timeSheetList;
        this.hmeCode = hmeCode;
        this.customerData = customerData;
        this.isIbauUser = isIbauUser;
        this.signerName = signerName;
        this.application = application;

        pdfCreated = new MutableLiveData<>(false);


        //Initialize
        timeSheetRepository = new TimeSheetRepository(application);
        hmecodeRepository = new HMECODERepository(application);


        times = application.getApplicationContext().getResources().getFont(R.font.times);
        timesBold = Typeface.create(times, 700, false);

        paintHeader.setTextSize(20);
        paintHeader.setTypeface(timesBold);

        paintText.setTextSize(10);
        paintText.setTypeface(times);

        paintBoldText.setTextSize(10);
        paintBoldText.setTypeface(Typeface.DEFAULT_BOLD);

        paintThickBlue.setStrokeWidth(3);
        paintThickBlue.setColor(application.getApplicationContext().getColor(R.color.blue));
        paintThickBlue.setStyle(Paint.Style.STROKE);

        paintBlue.setColor(application.getApplicationContext().getColor(R.color.blue));

        paintThick.setStrokeWidth(1);

        paintData.setTextSize(12);
        paintData.setTypeface(timesBold);

        options.inScaled = true;

        userSign = new File(application.getApplicationContext().getFilesDir() + "/signatures/userSign");
        customerSign = new File(application.getApplicationContext().getFilesDir() + "/signatures/" + hmeCode.getHMECode());

    }


    public void createPDF() {

        Executors.newSingleThreadExecutor().execute(() -> {

            //Page counter
            int currentPageCount = 0;
            int lastPageCreated = -1;

            for (int currentItem = 0; currentItem < timeSheetList.size(); currentItem++) {

                if (currentPageCount > lastPageCreated) {
                    pageInfo.add(new PdfDocument.PageInfo.Builder(595, 842, currentPageCount + 1).create());
                    page.add(pdfDocument.startPage(pageInfo.get(currentPageCount)));
                    canvas.add(page.get(currentPageCount).getCanvas());

                    createNewPage(currentPageCount);


                    //wait till new page created
                    lastPageCreated = currentPageCount;
                }


                if (currentItem + 1 == timeSheetList.size())
                    canvas.get(currentPageCount).drawLine(X_TABLE_LEFT, yPositionForCurrentItem + paintText.descent(), X_TABLE_LEFT + TABLE_WIDTH, yPositionForCurrentItem + paintText.descent(), paintThick);
                else
                    canvas.get(currentPageCount).drawLine(X_TABLE_LEFT, yPositionForCurrentItem + paintText.descent(), X_TABLE_LEFT + TABLE_WIDTH, yPositionForCurrentItem + paintText.descent(), paintText);


                canvas.get(currentPageCount).drawText(getDay(timeSheetList.get(currentItem).getDate()), X_TABLE_LEFT + DAY_SHIFT, yPositionForCurrentItem, paintText);

                canvas.get(currentPageCount).drawText(formatDateForPDF(timeSheetList.get(currentItem).getDate()), X_TABLE_LEFT + COLUMN_SHIFT_DATE + DATE_SHIFT, yPositionForCurrentItem, paintText);
                canvas.get(currentPageCount).drawText(timeSheetList.get(currentItem).getTravelStartTime(), X_TABLE_LEFT + 2 * COLUMN_SHIFT + TRAVEL_START_SHIFT, yPositionForCurrentItem, paintText);
                canvas.get(currentPageCount).drawText(timeSheetList.get(currentItem).getWorkStartTime(), X_TABLE_LEFT + 3 * COLUMN_SHIFT + WORK_START_SHIFT, yPositionForCurrentItem, paintText);
                canvas.get(currentPageCount).drawText(timeSheetList.get(currentItem).getWorkEndTime(), X_TABLE_LEFT + 4 * COLUMN_SHIFT + WORK_END_SHIFT, yPositionForCurrentItem, paintText);
                canvas.get(currentPageCount).drawText(timeSheetList.get(currentItem).getTravelEndTime(), X_TABLE_LEFT + 5 * COLUMN_SHIFT + TRAVEL_END_SHIFT, yPositionForCurrentItem, paintText);
                canvas.get(currentPageCount).drawText(timeSheetList.get(currentItem).getBreakHour(), X_TABLE_LEFT + 6 * COLUMN_SHIFT + BREAK_SHIFT, yPositionForCurrentItem, paintText);


                // Calculate work duration and travel

                if (timeSheetList.get(currentItem).getTravelStartTime().compareTo("---") == 0) {
                    canvas.get(currentPageCount).drawText("---", X_TABLE_LEFT + 7 * COLUMN_SHIFT + WORKING_HOURS_SHIFT, yPositionForCurrentItem, paintText);
                    canvas.get(currentPageCount).drawText("---", X_TABLE_LEFT + 9 * COLUMN_SHIFT + TRAVEL_HOURS_SHIFT, yPositionForCurrentItem, paintText);
                } else if (timeSheetList.get(currentItem).getWorkStartTime().compareTo("---") == 0) {
                    String[] hours = calculateHours(timeSheetList.get(currentItem).getTravelStartTime(), "00:00", "00:00", timeSheetList.get(currentItem).getTravelEndTime(), "0H",timeSheetList.get(currentItem).isOverTime());
                    canvas.get(currentPageCount).drawText(hours[0], X_TABLE_LEFT + 7 * COLUMN_SHIFT + WORKING_HOURS_SHIFT, yPositionForCurrentItem, paintText);
                    canvas.get(currentPageCount).drawText(hours[2], X_TABLE_LEFT + 8 * COLUMN_SHIFT + WORKING_HOURS_SHIFT, yPositionForCurrentItem, paintText);
                    canvas.get(currentPageCount).drawText(hours[1], X_TABLE_LEFT + 9 * COLUMN_SHIFT + TRAVEL_HOURS_SHIFT, yPositionForCurrentItem, paintText);
                } else {
                    String[] hours = calculateHours(timeSheetList.get(currentItem).getTravelStartTime(), timeSheetList.get(currentItem).getWorkStartTime(), timeSheetList.get(currentItem).getWorkEndTime(), timeSheetList.get(currentItem).getTravelEndTime(), timeSheetList.get(currentItem).getBreakHour(),timeSheetList.get(currentItem).isOverTime());
                    canvas.get(currentPageCount).drawText(hours[0], X_TABLE_LEFT + 7 * COLUMN_SHIFT + WORKING_HOURS_SHIFT, yPositionForCurrentItem, paintText);
                    canvas.get(currentPageCount).drawText(hours[2], X_TABLE_LEFT + 8 * COLUMN_SHIFT + WORKING_HOURS_SHIFT, yPositionForCurrentItem, paintText);
                    canvas.get(currentPageCount).drawText(hours[1], X_TABLE_LEFT + 9 * COLUMN_SHIFT + TRAVEL_HOURS_SHIFT, yPositionForCurrentItem, paintText);
                }

                canvas.get(currentPageCount).drawText(String.valueOf(timeSheetList.get(currentItem).getTravelDistance()), X_TABLE_LEFT + 10 * COLUMN_SHIFT + TRAVEL_DISTANCE_SHIFT, yPositionForCurrentItem, paintText);
                totalTravelDistance += timeSheetList.get(currentItem).getTravelDistance();
                timeSheetList.get(currentItem).setTimeSheetCreated(true);

                yPositionForCurrentItem += (textRowHeight);
                if (yPositionForCurrentItem > SIGNATURE_BOTTOM - textRowHeight - userSignHeight || currentItem == timeSheetList.size() - 1) {

                    // Draw Table Columns
                    for (int i = 0; i < 12; i++) {
                        if (i == 0 || i == 11) {
                            canvas.get(currentPageCount).drawLine(X_TABLE_LEFT + i * COLUMN_SHIFT, Y_TABLE_TOP + paintText.ascent(), X_TABLE_LEFT + i * COLUMN_SHIFT, yPositionForCurrentItem - textRowHeight + paintThick.descent(), paintThick);
                        } else if (i == 1) {
                            canvas.get(currentPageCount).drawLine(X_TABLE_LEFT + COLUMN_SHIFT_DATE, Y_TABLE_TOP + paintText.ascent(), X_TABLE_LEFT + COLUMN_SHIFT_DATE, yPositionForCurrentItem - textRowHeight + paintText.descent(), paintText);
                        } else {
                            canvas.get(currentPageCount).drawLine(X_TABLE_LEFT + i * COLUMN_SHIFT, Y_TABLE_TOP + paintText.ascent(), X_TABLE_LEFT + i * COLUMN_SHIFT, yPositionForCurrentItem - textRowHeight + paintText.descent(), paintText);
                        }
                    }

                    // End Current Page if new page will start

                    if (yPositionForCurrentItem > SIGNATURE_BOTTOM - textRowHeight - userSignHeight) {
                        pdfDocument.finishPage(page.get(currentPageCount));
                        yPositionForCurrentItem = Y_POSITION_START;
                        currentPageCount++;
                    }
                }


            }

            // Create totals
            canvas.get(currentPageCount).drawText("Total", X_TABLE_LEFT + 6 * COLUMN_SHIFT + TOTAL_SHIFT, yPositionForCurrentItem, paintBoldText);
            canvas.get(currentPageCount).drawText(String.valueOf(Math.round(totalWorkMinutes / 60.00 * 100) / 100f), X_TABLE_LEFT + 7 * COLUMN_SHIFT + WORKING_HOURS_SHIFT, yPositionForCurrentItem, paintText);
            canvas.get(currentPageCount).drawText(String.valueOf(Math.round(totalOverTime / 60.00 * 100) / 100f), X_TABLE_LEFT + 8 * COLUMN_SHIFT + OVER_TIME_SHIFT, yPositionForCurrentItem, paintText);
            canvas.get(currentPageCount).drawText(String.valueOf(Math.round(totalTravelMinutes / 60.00 * 100) / 100f), X_TABLE_LEFT + 9 * COLUMN_SHIFT + TRAVEL_HOURS_SHIFT, yPositionForCurrentItem, paintText);
            canvas.get(currentPageCount).drawText(String.valueOf(totalTravelDistance), X_TABLE_LEFT + 10 * COLUMN_SHIFT + TRAVEL_DISTANCE_SHIFT, yPositionForCurrentItem, paintText);

            for (int i = 7; i < 12; i++) {
                canvas.get(currentPageCount).drawLine(X_TABLE_LEFT + i * COLUMN_SHIFT, yPositionForCurrentItem + paintText.ascent() - paintText.descent(), X_TABLE_LEFT + i * COLUMN_SHIFT, yPositionForCurrentItem + paintText.descent(), paintThick);
            }

            canvas.get(currentPageCount).drawLine(X_TABLE_LEFT + 7 * COLUMN_SHIFT, yPositionForCurrentItem + paintText.descent(), X_TABLE_LEFT + 11 * COLUMN_SHIFT, yPositionForCurrentItem + paintText.descent(), paintThick);


            pdfDocument.finishPage(page.get(currentPageCount));
            customerSign.delete();

            // Save PDF

            File directory = new File(application.getApplicationContext().getFilesDir() + "/" + hmeCode.getHMECode());
            if (!directory.exists()) {
                directory.mkdirs();
            }

            try {
                if (hmeCode.getFileNumber() == 1) {
                    pdfDocument.writeTo(new FileOutputStream(new File(directory, hmeCode.getHMECode() + ".pdf")));
                } else {
                    int fileNumber = hmeCode.getFileNumber() - 1;
                    pdfDocument.writeTo(new FileOutputStream(new File(directory, hmeCode.getHMECode() + "_" + fileNumber + ".pdf")));
                }
                hmeCode.setFileNumber(hmeCode.getFileNumber() + 1);
                hmecodeRepository.update(hmeCode);
                pdfCreated.postValue(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            timeSheetRepository.update(timeSheetList);
            pdfDocument.close();
        });
    }


    private void createNewPage(int currentPageCount) {


        //Border
        canvas.get(currentPageCount).drawRoundRect(5, 5, 590, 838, 5, 5, paintThickBlue);


        canvas.get(currentPageCount).drawText("Time Sheet", 10, 5 + paintHeader.descent() - paintHeader.ascent(), paintHeader);


        // Header

        Bitmap HBLogo = BitmapFactory.decodeResource(application.getApplicationContext().getResources(), R.drawable.haver_middle_east, options);

        canvas.get(currentPageCount).drawBitmap(HBLogo, null, new RectF(LOGO_X_START, LOGO_Y_START, LOGO_X_END, LOGO_Y_END), null);


        canvas.get(currentPageCount).drawText("HAVER MIDDLE EAST FZE", LOGO_X_START, DATA_Y + paintData.descent() - paintData.ascent(), paintData);
        canvas.get(currentPageCount).drawText("P.O. Box 34098", LOGO_X_START + 45, DATA_Y + 2 * (paintData.descent() - paintData.ascent()), paintData);
        canvas.get(currentPageCount).drawText("Ras Al Khaimah", LOGO_X_START + 45, DATA_Y + 3 * (paintData.descent() - paintData.ascent()), paintData);
        canvas.get(currentPageCount).drawText("United Arab Emirates", LOGO_X_START + 35, DATA_Y + 4 * (paintData.descent() - paintData.ascent()), paintData);
        canvas.get(currentPageCount).drawText("Phone +971 7 20 680 00", LOGO_X_START + 30, DATA_Y + 5 * (paintData.descent() - paintData.ascent()), paintData);
        canvas.get(currentPageCount).drawText("Fax +971 7 20 680 09", LOGO_X_START + 35, DATA_Y + 6 * (paintData.descent() - paintData.ascent()), paintData);
        canvas.get(currentPageCount).drawText("office@havermiddleeast.com", LOGO_X_START + 10, DATA_Y + 7 * (paintData.descent() - paintData.ascent()), paintData);


        canvas.get(currentPageCount).drawText("Customer:", LEFT_DATA_START, DATA_TOP, paintBoldText);
        canvas.get(currentPageCount).drawText("Project City:", LEFT_DATA_START, DATA_TOP + 2 * (textRowHeight), paintBoldText);
        canvas.get(currentPageCount).drawText("Machine Type:", LEFT_DATA_START, DATA_TOP + 4 * (textRowHeight), paintBoldText);
        canvas.get(currentPageCount).drawText("Departure:", LEFT_DATA_START, DATA_TOP + 6 * (textRowHeight), paintBoldText);
        canvas.get(currentPageCount).drawText("HME Mission Code:", LEFT_DATA_START, DATA_TOP + 8 * (textRowHeight), paintBoldText);
        canvas.get(currentPageCount).drawText("Work Description:", LEFT_DATA_START, DATA_TOP + 10 * (textRowHeight), paintBoldText);

        for (int i = 0; i < 6; i++) {
            canvas.get(currentPageCount).drawLine(LEFT_DATA_START, DATA_TOP + paintText.descent() + 2 * i * (textRowHeight), 410, 50 + paintText.descent() + 2 * i * (textRowHeight), paintBlue);
        }


        canvas.get(currentPageCount).drawText("Service Engineer:", RIGHT_DATA_START, DATA_TOP, paintBoldText);
        canvas.get(currentPageCount).drawText("Project Country:", RIGHT_DATA_START, DATA_TOP + 2 * (textRowHeight), paintBoldText);
        canvas.get(currentPageCount).drawText("Machine Serial:", RIGHT_DATA_START, DATA_TOP + 4 * (textRowHeight), paintBoldText);
        canvas.get(currentPageCount).drawText("Arrival:", RIGHT_DATA_START, DATA_TOP + 6 * (textRowHeight), paintBoldText);


        //get settings
        SharedPreferences settings = application.getApplicationContext().getSharedPreferences(Settings, MODE_PRIVATE);

        canvas.get(currentPageCount).drawText(customerData.getCustomerName(), LEFT_DATA_START + CUSTOMER_SHIFT, DATA_TOP, paintText);
        canvas.get(currentPageCount).drawText(customerData.getCustomerCity(), LEFT_DATA_START + CITY_SHIFT, DATA_TOP + 2 * (textRowHeight), paintText);
        canvas.get(currentPageCount).drawText(hmeCode.getMachineType(), LEFT_DATA_START + MACHINE_TYPE_SHIFT, DATA_TOP + 4 * (textRowHeight), paintText);
        canvas.get(currentPageCount).drawText(timeSheetList.get(0).getDate() + "   " + timeSheetList.get(0).getTravelStartTime(), LEFT_DATA_START + DEPARTURE_SHIFT, DATA_TOP + 6 * (textRowHeight), paintText);
        canvas.get(currentPageCount).drawText(hmeCode.getHMECode(), LEFT_DATA_START + HME_CODE_SHIFT, DATA_TOP + 8 * (textRowHeight), paintText);
        canvas.get(currentPageCount).drawText(hmeCode.getDescriptionWork(), LEFT_DATA_START + WORK_DESCRIPTION_SHIFT, DATA_TOP + 10 * (textRowHeight), paintText);
        canvas.get(currentPageCount).drawText(settings.getString(UserName, ""), RIGHT_DATA_START + SERVICE_ENGINEER_SHIFT, DATA_TOP, paintText);
        canvas.get(currentPageCount).drawText(customerData.getCustomerCountry(), RIGHT_DATA_START + COUNTRY_SHIFT, DATA_TOP + 2 * (textRowHeight), paintText);
        canvas.get(currentPageCount).drawText(hmeCode.getMachineNumber(), RIGHT_DATA_START + MACHINE_NUMBER_SHIFT, DATA_TOP + 4 * (textRowHeight), paintText);
        canvas.get(currentPageCount).drawText(timeSheetList.get(timeSheetList.size() - 1).getDate() + "   " + timeSheetList.get(timeSheetList.size() - 1).getTravelEndTime(), RIGHT_DATA_START + ARRIVAL_SHIFT, DATA_TOP + 6 * (textRowHeight), paintText);

        if (settings.getBoolean(IBAUUser, false) && timeSheetList.get(0).getIBAUSO() != null) {
            canvas.get(currentPageCount).drawText("IBAU Service Order:", RIGHT_DATA_START, DATA_TOP + 8 * (textRowHeight), paintBoldText);
            canvas.get(currentPageCount).drawText(timeSheetList.get(0).getIBAUSO(), RIGHT_DATA_START + IBAU_SO_SHIFT, DATA_TOP + 8 * (textRowHeight), paintText);
        }

        // --- Create Table Header ---
        // Draw Lines
        canvas.get(currentPageCount).drawLine(X_TABLE_LEFT, Y_TABLE_TOP + paintText.ascent(), X_TABLE_LEFT + TABLE_WIDTH, Y_TABLE_TOP + paintText.ascent(), paintThick);
        canvas.get(currentPageCount).drawLine(X_TABLE_LEFT, Y_TABLE_TOP + textRowHeight + paintThick.descent(), X_TABLE_LEFT + TABLE_WIDTH, Y_TABLE_TOP + textRowHeight + paintThick.descent(), paintThick);

        //Day
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.day), X_TABLE_LEFT + DAY_HEADER_SHIFT, Y_TABLE_TOP, paintBoldText);

        //Date
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.date), X_TABLE_LEFT + COLUMN_SHIFT_DATE + DATE_HEADER_SHIFT, Y_TABLE_TOP, paintBoldText);

        //Travel Start
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.travel), X_TABLE_LEFT + 2 * COLUMN_SHIFT + TRAVEL_START_HEADER_SHIFT, Y_TABLE_TOP, paintBoldText);
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.start), X_TABLE_LEFT + 2 * COLUMN_SHIFT + TRAVEL_START_HEADER_SHIFT, Y_TABLE_TOP + textRowHeight, paintBoldText);

        //Work Start
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.work), X_TABLE_LEFT + 3 * COLUMN_SHIFT + WORK_START_HEADER_SHIFT, Y_TABLE_TOP, paintBoldText);
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.start), X_TABLE_LEFT + 3 * COLUMN_SHIFT + WORK_START_HEADER_SHIFT, Y_TABLE_TOP + textRowHeight, paintBoldText);

        //Work End
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.work), X_TABLE_LEFT + 4 * COLUMN_SHIFT + WORK_END_HEADER_SHIFT, Y_TABLE_TOP, paintBoldText);
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.end), X_TABLE_LEFT + 4 * COLUMN_SHIFT + WORK_END_HEADER_SHIFT, Y_TABLE_TOP + textRowHeight, paintBoldText);

        //Travel End
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.travel), X_TABLE_LEFT + 5 * COLUMN_SHIFT + TRAVEL_END_HEADER_SHIFT, Y_TABLE_TOP, paintBoldText);
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.end), X_TABLE_LEFT + 5 * COLUMN_SHIFT + TRAVEL_END_HEADER_SHIFT, Y_TABLE_TOP + textRowHeight, paintBoldText);

        //Break Duration
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.break__), X_TABLE_LEFT + 6 * COLUMN_SHIFT + BREAK_HEADER_SHIFT, Y_TABLE_TOP, paintBoldText);
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.duration), X_TABLE_LEFT + 6 * COLUMN_SHIFT + BREAK_HEADER_SHIFT, Y_TABLE_TOP + textRowHeight, paintBoldText);

        //working Hours
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.work), X_TABLE_LEFT + 7 * COLUMN_SHIFT + WORKING_HOURS_HEADER_SHIFT, Y_TABLE_TOP, paintBoldText);
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.hours), X_TABLE_LEFT + 7 * COLUMN_SHIFT + WORKING_HOURS_HEADER_SHIFT, Y_TABLE_TOP + textRowHeight, paintBoldText);

        //OverTime Hours
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.overtime), X_TABLE_LEFT + 8 * COLUMN_SHIFT + OVER_TIME_HEADER_SHIFT, Y_TABLE_TOP, paintBoldText);
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.hours), X_TABLE_LEFT + 8 * COLUMN_SHIFT + OVER_TIME_HEADER_SHIFT, Y_TABLE_TOP + textRowHeight, paintBoldText);

        //Travel Hours
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.travel), X_TABLE_LEFT + 9 * COLUMN_SHIFT + TRAVEL_HOURS_HEADER_SHIFT, Y_TABLE_TOP, paintBoldText);
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.hours), X_TABLE_LEFT + 9 * COLUMN_SHIFT + TRAVEL_HOURS_HEADER_SHIFT, Y_TABLE_TOP + textRowHeight, paintBoldText);

        //Travel Distance
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.travel), X_TABLE_LEFT + 10 * COLUMN_SHIFT + TRAVEL_DISTANCE_HEADER_SHIFT, Y_TABLE_TOP, paintBoldText);
        canvas.get(currentPageCount).drawText(application.getApplicationContext().getResources().getString(R.string.distance), X_TABLE_LEFT + 10 * COLUMN_SHIFT + TRAVEL_DISTANCE_HEADER_SHIFT, Y_TABLE_TOP + textRowHeight, paintBoldText);

        // User signature


        if (userSign.exists()) {
            Bitmap UserSign = BitmapFactory.decodeFile(userSign.getPath(), options);
            float ratio = ((float) UserSign.getHeight() / (float) UserSign.getWidth());

            userSignHeight = (int) (ENGINEER_SIGN_WIDTH * ratio);

            canvas.get(currentPageCount).drawBitmap(UserSign, null, new RectF(ENGINEER_SIGNATURE_IMG_SHIFT, SIGNATURE_BOTTOM - textRowHeight - userSignHeight, ENGINEER_SIGNATURE_IMG_SHIFT + ENGINEER_SIGN_WIDTH, SIGNATURE_BOTTOM - textRowHeight), null);
        }

        // Customer signature


        if (customerSign.exists()) {
            Bitmap CustomerSign = BitmapFactory.decodeFile(customerSign.getPath(), options);
            float ratio = ((float) CustomerSign.getHeight() / (float) CustomerSign.getWidth());
            customerSignHeight = (int) (CUSTOMER_SIGN_WIDTH * ratio);

            for (int i = 0; i <= currentPageCount; i++) {
                canvas.get(currentPageCount).drawBitmap(CustomerSign, null, new RectF(CUSTOMER_SIGNATURE_IMG_SHIFT, SIGNATURE_BOTTOM - textRowHeight - customerSignHeight, CUSTOMER_SIGN_SHIFT + CUSTOMER_SIGN_WIDTH + CUSTOMER_SIGN_WIDTH, SIGNATURE_BOTTOM - textRowHeight), null);
                canvas.get(currentPageCount).drawText(signerName + "   " + getTodayDate(), CUSTOMER_SIGNATURE_DATE_SHIFT, SIGNATURE_BOTTOM + paintText.ascent() - 2 * paintText.descent(), paintText);
            }
        }
        else if(!customerSign.exists()){
            for (int i = 0; i <= currentPageCount; i++) {
                canvas.get(currentPageCount).drawText(signerName, CUSTOMER_SIGNATURE_DATE_SHIFT, SIGNATURE_BOTTOM + paintText.ascent() - 2 * paintText.descent(), paintText);
            }
        }

        // --- Signature ---
        //Checked by HME
        canvas.get(currentPageCount).drawText("Check by HME", CHECKED_SHIFT, SIGNATURE_BOTTOM, paintText);
        canvas.get(currentPageCount).drawLine(CHECKED_SHIFT + SIGNATURE_LINE_SHIFT, SIGNATURE_BOTTOM + paintText.ascent() - paintText.descent(), CHECKED_SHIFT + CHECKED_LINE_LENGTH + SIGNATURE_LINE_SHIFT, SIGNATURE_BOTTOM + paintText.ascent() - paintText.descent(), paintText);

        // Customer name and Sign
        canvas.get(currentPageCount).drawText("Customer Signature", CUSTOMER_SIGN_SHIFT, SIGNATURE_BOTTOM, paintText);
        canvas.get(currentPageCount).drawLine(CUSTOMER_SIGN_SHIFT + SIGNATURE_LINE_SHIFT, SIGNATURE_BOTTOM + paintText.ascent() - paintText.descent(), CUSTOMER_SIGN_SHIFT + CUSTOMER_LINE_LENGTH + SIGNATURE_LINE_SHIFT, SIGNATURE_BOTTOM + paintText.ascent() - paintText.descent(), paintText);

        //Engineer Sign
        canvas.get(currentPageCount).drawText("Engineer Signature", ENGINEER_SIGN_SHIFT, SIGNATURE_BOTTOM, paintText);
        canvas.get(currentPageCount).drawLine(ENGINEER_SIGN_SHIFT + SIGNATURE_LINE_SHIFT, SIGNATURE_BOTTOM + paintText.ascent() - paintText.descent(), ENGINEER_SIGN_SHIFT + ENGINEER_LINE_LENGTH + SIGNATURE_LINE_SHIFT, SIGNATURE_BOTTOM + paintText.ascent() - paintText.descent(), paintText);
        canvas.get(currentPageCount).drawText(getTodayDate(), ENGINEER_SIGNATURE_DATE_SHIFT, SIGNATURE_BOTTOM + paintText.ascent() - 2 * paintText.descent(), paintText);


    }

    @SuppressLint("DefaultLocale")
    private String[] calculateHours(String travelStartTime, String workStartTime, String workEndTime, String travelEndTime, String breakTime, boolean overTime) {
        String[] travelStartArray = travelStartTime.split(":");
        int travelStartMinutes = (Integer.parseInt(travelStartArray[0])) * 60 + Integer.parseInt(travelStartArray[1]);

        String[] workStartArray = workStartTime.split(":");
        int workStartMinutes = (Integer.parseInt(workStartArray[0])) * 60 + Integer.parseInt(workStartArray[1]);

        String[] workEndArray = workEndTime.split(":");
        int workEndMinutes = (Integer.parseInt(workEndArray[0])) * 60 + Integer.parseInt(workEndArray[1]);

        String[] travelEndArray = travelEndTime.split(":");
        int travelEndMinutes = (Integer.parseInt(travelEndArray[0])) * 60 + Integer.parseInt(travelEndArray[1]);

        String[] breakTimeArray = breakTime.replace("H", "").split("\\.");

        int breakTimeMinutes;


        if (breakTimeArray.length == 2) {
            breakTimeMinutes = (Integer.parseInt(breakTimeArray[0])) * 60 + Integer.parseInt(breakTimeArray[1]) * 6;
        } else {
            breakTimeMinutes = (Integer.parseInt(breakTimeArray[0])) * 60;
        }

        int workTimeMinutes = 0;
        int overTimeMinutes = 0;
        if (overTime) {
            overTimeMinutes = workEndMinutes - workStartMinutes - breakTimeMinutes;
        } else {
            workTimeMinutes = workEndMinutes - workStartMinutes - breakTimeMinutes;
            if (workTimeMinutes >= 8 * 60) {
                overTimeMinutes = workTimeMinutes - (8 * 60);
                workTimeMinutes = 8 * 60;
            }
        }
        int travelTimeMinutes = travelEndMinutes - travelStartMinutes - workTimeMinutes - overTimeMinutes - breakTimeMinutes;


        float calWorkTime = (float) Math.round(workTimeMinutes / 60.0 * 100) / 100;
        float calTravelTime = (float) Math.round(travelTimeMinutes / 60.00 * 100) / 100;
        float calOverTime = (float) Math.round(overTimeMinutes / 60.00 * 100) / 100;

        totalTravelMinutes += travelTimeMinutes;
        totalWorkMinutes += workTimeMinutes;
        totalOverTime += overTimeMinutes;

        String[] calculatedHours = new String[3];
        calculatedHours[0] = String.format("%.2f", calWorkTime);
        calculatedHours[1] = String.format("%.2f", calTravelTime);
        calculatedHours[2] = String.format("%.2f", calOverTime);


        return calculatedHours;

    }


    private String getDay(String date) {
        Calendar calendar = DateFormatter.formatDateToCalendar(date);
        assert calendar != null;
        return calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT_STANDALONE, Locale.ENGLISH);

    }


    @SuppressLint("DefaultLocale")
    private String getTodayDate() {
        final Calendar c = Calendar.getInstance();
        return dateFormat.format(c.getTime());
    }

    String formatDateForPDF(String dateString) {
        String[] dateSplit = dateString.split("/");
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.set(Integer.parseInt(dateSplit[0]), Integer.parseInt(dateSplit[1]) - 1, Integer.parseInt(dateSplit[2]));

        return dateFormat.format(dateCalendar.getTime());
    }

}
