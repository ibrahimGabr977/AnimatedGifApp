package com.hope.igb.catgif.screens.comman.dialoghelper;


import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import com.hope.igb.blinklibrary.SweetAlertDialog;
import com.hope.igb.catgif.R;
import com.hope.igb.catgif.comman.BaseObservable;


/**
 * this class for showing the different details dialogs(help) about app features on all app screens
 */

public class InfoDialogHelper extends BaseObservable<InfoDialogHelper.ReviewUsClickListener> {

    private final Context context;
    private final SweetAlertDialog alertDialog;


    public interface ReviewUsClickListener{
        void onReviewUsClicked();
    }



    public InfoDialogHelper(Context context) {
        this.context = context;



        alertDialog = new SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE, R.style.alert_dialog_light);

        alertDialog.setCustomImage(R.drawable.z_dialog_image);

        alertDialog.setConfirmButtonBackgroundColor(Color.parseColor("#2a3650"));
        alertDialog.setContentTextSize(14);
        alertDialog.setConfirmButtonTextColor(Color.parseColor("#fafcfe"));

    }




    public SweetAlertDialog appHelpDialog(){
        return alertDialog.setTitleText(context.getString(R.string.app_help_title))
                .setContentText(context.getString(R.string.app_help_body))
                //change button text to review us
                .setConfirmButton(context.getString(R.string.nav_review_us),


                        //handle review us click listener
                        sweetAlertDialog -> {

                            alertDialog.dismissWithAnimation();

                            for (ReviewUsClickListener listener: getListeners())
                                listener.onReviewUsClicked(); });


    }




    public SweetAlertDialog selectActivityDialog(){

         return alertDialog.setTitleText(context.getResources().getString(R.string.select_activity_help_title))
                .setContentText(context.getResources().getString(R.string.select_activity_help_body));
    }


    public SweetAlertDialog generateActivityDialog(String fragment_name){
        return  alertDialog.setTitleText(context.getString(R.string.generate_activity_help_title))

                //check the current fragment to show the desired help text
                .setContentText(context.getString(fragment_name.equals("GenerateActivityVideo") ?
                        R.string.generate_video_activity_help_body : R.string.generate_images_activity_help_body));


    }



    public SweetAlertDialog versionHelpDialog(boolean isImproved){
        alertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        alertDialog.setTitleText(context.getString(isImproved ? R.string.briefly_version : R.string.default_version));


        //check the clicked version to show the desired help text
        return alertDialog.setContentText(context.getString(isImproved ?
                        R.string.generate_video_activity_briefly_version :
                R.string.generate_video_activity_default_version));
    }




    public SweetAlertDialog libraryHelpDialog(){

        return alertDialog.setCustomImage(R.drawable.z_review_us_5_stars).
                setTitleText(context.getString(R.string.library_activity_help_title))

                .setContentText(context.getString(R.string.library_activity_help_body))


                //change button text to review us
                .setConfirmButton(context.getString(R.string.nav_review_us),


                        //handle review us click listener
                        sweetAlertDialog -> {

                            alertDialog.dismissWithAnimation();

                    for (ReviewUsClickListener listener: getListeners())
                        listener.onReviewUsClicked(); });
    }




    public SweetAlertDialog onlineGifInfo(String info){
        alertDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
        alertDialog.setTitleText(context.getString(R.string.gif_details));


        //check the clicked version to show the desired help text
        return alertDialog.setContentText(extractOnlineGifInfo(info));
    }




    //to put a new line after each detail
    private String extractOnlineGifInfo(String info){
        String [] info_splitter = info.split("#");
        StringBuilder correct_info = new StringBuilder();
        for (String s: info_splitter){
            correct_info.append(Html.fromHtml(s + "&lt;br /&gt;").toString());
        }

        return String.valueOf(correct_info);
    }

}
