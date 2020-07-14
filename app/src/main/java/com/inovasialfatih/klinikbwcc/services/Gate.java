package com.inovasialfatih.klinikbwcc.services;

import android.content.Context;

import com.inovasialfatih.klinikbwcc.services.infrastructure.RetrofitCall;
import com.inovasialfatih.klinikbwcc.services.iservices.IContactService;
import com.inovasialfatih.klinikbwcc.services.iservices.IDoctorScheduleService;
import com.inovasialfatih.klinikbwcc.services.iservices.IHistoryService;
import com.inovasialfatih.klinikbwcc.services.iservices.INewsService;
import com.inovasialfatih.klinikbwcc.services.iservices.IPatientService;
import com.inovasialfatih.klinikbwcc.services.iservices.IRequestBookingService;
import com.inovasialfatih.klinikbwcc.services.iservices.IRequestRmService;
import com.inovasialfatih.klinikbwcc.services.iservices.IScheduleService;
import com.inovasialfatih.klinikbwcc.services.iservices.IPaymentService;
import com.inovasialfatih.klinikbwcc.services.iservices.ISliderBannerService;
import com.inovasialfatih.klinikbwcc.services.iservices.IUserService;

public class Gate extends RetrofitCall {
    public Gate(Context context) {
        super(context, true);
    }

    public IUserService user(){
        return callIgnoreAuth().create(IUserService.class);
    }

    public IScheduleService schedule(){
        return callIgnoreAuth().create(IScheduleService.class);
    }

    public IRequestRmService requestRm(){
        return callIgnoreAuth().create(IRequestRmService.class);
    }

    public IRequestBookingService requestBooking(){
        return callIgnoreAuth().create(IRequestBookingService.class);
    }

    public IDoctorScheduleService doctorSchedule() {
        return callIgnoreAuth().create(IDoctorScheduleService.class);
    }

    public IHistoryService history(){
        return callIgnoreAuth().create(IHistoryService.class);
    }

    public ISliderBannerService sliderBanner(){
        return callIgnoreAuth().create(ISliderBannerService.class);
    }

    public INewsService news(){
        return callIgnoreAuth().create(INewsService.class);
    }

    public IPatientService patient() {
        return callIgnoreAuth().create(IPatientService.class);
    }

    public IContactService contactUs() {
        return callIgnoreAuth().create(IContactService.class);
    }

    public IPaymentService payment() {
        return callIgnoreAuth().create(IPaymentService.class);
    }

}
