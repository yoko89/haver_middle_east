package com.neklaway.havermiddle_east.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "HMECODE_TABLE",indices = @Index(value = {"HME_CODE"},unique = true))
public class HMECode {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    private int id;

    @ColumnInfo(name = "HME_CODE")
    private final String HMECode;

    @ColumnInfo(name = "CUSTOMER_NAME")
    private final String customerName;

    @ColumnInfo(name = "MACHINE_TYPE")
    private final String machineType;

    @ColumnInfo(name = "MACHINE_NUMBER")
    private final String machineNumber;

    @ColumnInfo(name = "WORK_DESCRIPTION")
    private final String descriptionWork;

    @ColumnInfo(name = "FILE_NUMBER")
    private int fileNumber;


    public HMECode(String HMECode, String customerName, String machineType, String machineNumber, String descriptionWork, int fileNumber) {
        this.HMECode = HMECode;
        this.customerName = customerName;
        this.machineType = machineType;
        this.machineNumber = machineNumber;
        this.descriptionWork = descriptionWork;
        this.fileNumber = fileNumber;
    }

// --Commented out by Inspection START (05-Oct-21 12:50 PM):
//    public static String toString(com.neklaway.havermiddle_east.entities.HMECode HMECode) {
//        return  HMECode.HMECode +
//                "\n" + HMECode.machineType +
//                ", " + HMECode.machineNumber +
//                "\n" + HMECode.descriptionWork ;
//    }
// --Commented out by Inspection STOP (05-Oct-21 12:50 PM)

    @Override
    public String toString() {
        return  HMECode +
                "\n" + machineType +
                ", " + machineNumber +
                "\n" + descriptionWork ;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getHMECode() {
        return HMECode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getMachineType() {
        return machineType;
    }

    public String getMachineNumber() {
        return machineNumber;
    }

    public String getDescriptionWork() {
        return descriptionWork;
    }

    public int getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(int fileNumber) {
        this.fileNumber = fileNumber;
    }
}
