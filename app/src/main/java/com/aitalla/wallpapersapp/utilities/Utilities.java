package com.aitalla.wallpapersapp.utilities;

import static com.aitalla.wallpapersapp.config.AppConfig.*;

import android.app.Activity;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.aitalla.wallpapersapp.R;
import com.aitalla.wallpapersapp.adapters.CategoryAdapter;
import com.aitalla.wallpapersapp.adapters.WallpaperAdapter;
import com.aitalla.wallpapersapp.listeners.Listener;
import com.aitalla.wallpapersapp.listeners.OnCategoryClickListener;
import com.aitalla.wallpapersapp.listeners.OnWallpaperClickListener;
import com.aitalla.wallpapersapp.models.Category;
import com.aitalla.wallpapersapp.models.Wallpaper;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.mbridge.msdk.MBridgeConstans;
import com.mbridge.msdk.MBridgeSDK;

import com.mbridge.msdk.newinterstitial.out.MBNewInterstitialHandler;
import com.mbridge.msdk.newinterstitial.out.NewInterstitialListener;

import com.mbridge.msdk.out.MBRewardVideoHandler;
import com.mbridge.msdk.out.MBridgeIds;
import com.mbridge.msdk.out.MBridgeSDKFactory;

import com.mbridge.msdk.out.RewardInfo;
import com.mbridge.msdk.out.RewardVideoListener;
import com.mbridge.msdk.out.SDKInitStatusListener;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collections;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Utilities {
    public static ArrayList<Wallpaper> wallpapers = new ArrayList<>();


    public static void toast(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }

    public static String[] getTabs(){
       return showCategory ? new String[]{"Category", "Home", "Favourites"} : new String[]{"Home", "Favourites"};
    }

    public static void setStatusBrandNavigationBarColorPrimary(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.primary));
            activity.getWindow().setNavigationBarColor(ContextCompat.getColor(activity, R.color.primary));
        }
    }
    public static boolean isMaxScrollReached(RecyclerView recyclerView) {
        int maxScroll = recyclerView.computeVerticalScrollRange();
        int currentScroll = recyclerView.computeVerticalScrollOffset() + recyclerView.computeVerticalScrollExtent();
        return currentScroll >= maxScroll;

    }
    public static void getPeakPxData(Activity activity, RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout, Listener listener, String url){
        wallpapers.clear();
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());
        ProgressDialog.showProgressDialog(activity);
        executor.execute(()->{
            try {
                Document document = Jsoup.connect(url).userAgent("firefox").followRedirects(false).get();
                Elements elements = document.select(".lst_img");
                for(Element element : elements){
                    Wallpaper wallpaper = new Wallpaper(element.attr("data-src").replace("-thumbnail.jpg",".jpg"),false);
                     wallpapers.add(wallpaper);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            handler.post(()->{

                for(Wallpaper wallpaper : wallpapers){
                    Random r = new Random();
                    boolean premium = (r.nextInt(wallpapers.size()-1) % 2 == 0);
                    wallpaper.setPremium(premium);
                }

                Collections.shuffle(wallpapers);
                WallpaperAdapter adapter = new WallpaperAdapter(activity,wallpapers,(OnWallpaperClickListener)listener);
                RecyclerView.LayoutManager manager = new GridLayoutManager(activity,3);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
                ProgressDialog.hideProgressDialog();
            });
        });
    }

    public static void readData(Activity activity){
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, configUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {

                    JSONObject appControl =  jsonObject.getJSONObject("app_control");
                    JSONObject appData =  jsonObject.getJSONObject("app_data");
                    rateUs = appData.getString("rate");
                    share = appData.getString("share");
                    privacyPolicy = appData.getString("privacy_policy");
                    JSONObject adsManager =  jsonObject.getJSONObject("ads_manager");

                    showData=Boolean.parseBoolean(appControl.getString("show_data"));
                    showAds=Boolean.parseBoolean(appControl.getString("show_ads"));
                    showCategory=Boolean.parseBoolean(appControl.getString("show_category"));
                    useMintegral=Boolean.parseBoolean(appControl.getString("use_mintegral"));
                    useAdmob=Boolean.parseBoolean(appControl.getString("use_admob"));
                    useUnity=Boolean.parseBoolean(appControl.getString("use_unity"));

                    JSONObject mintegral =  adsManager.getJSONObject("mintegral");
                    JSONObject bannerAd = mintegral.getJSONObject("banner");
                    JSONObject interstitialAd = mintegral.getJSONObject("interstitial");
                    JSONObject nativeAd = mintegral.getJSONObject("native");
                    JSONObject rewardAd = mintegral.getJSONObject("reward");
                    mintegralAppID = mintegral.getString("app_id");
                    mintegralAppKey = mintegral.getString("app_key");
                    mintegralBannerUnitID = bannerAd.getString("unit_id");
                    mintegralBannerPlacementID = bannerAd.getString("placement_id");
                    mintegralInterUnitID = interstitialAd.getString("unit_id");
                    mintegralInterPlacementID = interstitialAd.getString("placement_id");
                    mintegralNativeUnitID = nativeAd.getString("unit_id");
                    mintegralNativePlacementID = nativeAd.getString("placement_id");
                    mintegralRewardUnitID = rewardAd.getString("unit_id");
                    mintegralRewardPlacementID = rewardAd.getString("placement_id");
                    JSONArray jsonCategories = appData.getJSONArray("categories");
                    for (int i = 0; i < jsonCategories.length(); i++) {
                        JSONObject jsonCategory = jsonCategories.getJSONObject(i);
                        String imageUrl = jsonCategory.getString("imageUrl");
                        String name = jsonCategory.getString("name");
                        String keywords = jsonCategory.getString("keywords");
                        categories.add(new Category(imageUrl, name, keywords));

                    }

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 3000);


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Utilities.toast(activity,"Connection Error !! Please Restart");
            }
        });

        requestQueue.add(jsonObjectRequest);
    }
    public static void readCategories(Activity activity , RecyclerView recyclerView, Listener listener){
        ProgressDialog.showProgressDialog(activity);
        Collections.shuffle(categories);
        CategoryAdapter adapter = new CategoryAdapter(activity,(OnCategoryClickListener)listener,categories);
        RecyclerView.LayoutManager manager = new GridLayoutManager(activity,1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        ProgressDialog.hideProgressDialog();
    }

    public static void initSdk(Activity activity){
        MBridgeSDK sdk = MBridgeSDKFactory.getMBridgeSDK();
        Map<String, String> map = sdk.getMBConfigurationMap(mintegralAppID,mintegralAppKey);
        sdk.init(map, activity, new SDKInitStatusListener() {
            @Override
            public void onInitSuccess() {
                //toast(activity,"//// INIT SDK SUCCESS ////");
            }
            @Override
            public void onInitFail(String errorMsg) {
                // Toast.makeText(MainActivity.this, "//// INIT SDK FAIL ////  "+errorMsg, Toast.LENGTH_SHORT).show(); Log.e("SDKInitStatus", "onInitSuccess");

                Log.e("SDKInitStatusInitFail", errorMsg);
            }
        });
    }

    public static void showMintegralInterstitial(Activity activity){
        if(showAds && useMintegral){

            MBNewInterstitialHandler mMBInterstitalVideoHandler = new MBNewInterstitialHandler(activity, mintegralInterPlacementID,mintegralInterUnitID);

            mMBInterstitalVideoHandler.setInterstitialVideoListener(new NewInterstitialListener() {

                private static final String TAG = "NEW INTER";

                @Override
                public void onLoadCampaignSuccess(MBridgeIds ids) {
                    /**
                     * Ad has been filled
                     * @param ids Encapsulated ad ID object
                     */
                    Log.i(TAG, "onLoadCampaignSuccess: " + Thread.currentThread() + " " + ids.toString());
                }

                @Override
                public void onResourceLoadSuccess(MBridgeIds ids) {
                    /**
                     * Ad resources loaded successfully, ready to play
                     * @param ids Encapsulated ad ID object
                     */
                    Log.i(TAG, "onResourceLoadSuccess: " + Thread.currentThread() + " " + ids.toString());
                }

                @Override
                public void onResourceLoadFail(MBridgeIds ids, String errorMsg) {
                    /**
                     * Ad loading failed
                     * @param errorMsg The reason for the loading error
                     */
                    Log.e(TAG, "onResourceLoadFail errorMsg: " + errorMsg + " " + ids.toString());
                }

                @Override
                public void onShowFail(MBridgeIds ids, String errorMsg) {
                    /**
                     * Ad playback failed
                     * @param errorMsg The reason for the error
                     */
                    Log.e(TAG, "onShowFail: " + errorMsg + " " + ids.toString());
                }

                @Override
                public void onAdShow(MBridgeIds ids) {
                    /**
                     * Ad successfully displayed
                     */
                    Log.i(TAG, "onAdShow: "+ ids.toString());
                }

                @Override
                public void onAdClose(MBridgeIds ids, RewardInfo info) {
                    /**
                     * Called when the ad is closed
                     * @param info.isCompleteView If true, it means the video has been fully watched
                     */
                    Log.i(TAG, "onAdClose: " +  "isCompleteView：" + info.isCompleteView() + " " + ids.toString());
                }

                @Override
                public void onAdClicked(MBridgeIds ids) {
                    /**
                     * Ad has been clicked
                     * @param Encapsulated ad ID object
                     */
                    Log.i(TAG, "onAdClicked: " + ids.toString());
                }

                @Override
                public void onVideoComplete(MBridgeIds ids) {
                    /**
                     * Called when the ad video playback is complete
                     * @param ids Encapsulated ad ID object
                     */
                    Log.i(TAG,"onVideoComplete: " + ids.toString());
                }

                @Override
                public void onAdCloseWithNIReward(MBridgeIds ids, RewardInfo info) {
                    /**
                     * Called when the ad is closed if the developer has set IV rewards.
                     *
                     * @param Encapsulated ad ID object
                     * @param info.isCompleteView() Whether the video is fully watched
                     */
                    Log.i(TAG, "onAdCloseWithNIReward: " + ids.toString() + "  " + info.toString());

                    Log.i(TAG, info.isCompleteView() ? "Video playback/playable is complete." : "Video playback/playable is not complete.");

                    int rewardAlertStatus = info.getRewardAlertStatus();

                    if (rewardAlertStatus == MBridgeConstans.IVREWARDALERT_STATUS_NOTSHOWN) {
                        Log.e(TAG,"The dialog is not shown.");
                    }

                    if (rewardAlertStatus == MBridgeConstans.IVREWARDALERT_STATUS_CLICKCONTINUE) {
                        Log.e(TAG,"The dialog's continue button clicked.");
                    }

                    if (rewardAlertStatus == MBridgeConstans.IVREWARDALERT_STATUS_CLICKCANCEL) {
                        Log.e(TAG,"The dialog's cancel button clicked.");
                    }
                }

                @Override
                public void onEndcardShow(MBridgeIds ids) {
                    /**
                     * Called when the ad landing page is shown
                     * @param ids Encapsulated ad ID object
                     */
                    Log.i(TAG,"onEndcardShow: " + ids.toString());
                }

            });

            mMBInterstitalVideoHandler.playVideoMute(MBridgeConstans.REWARD_VIDEO_PLAY_MUTE);

            mMBInterstitalVideoHandler.setIVRewardEnable(MBridgeConstans.IVREWARD_TYPE_CLOSEMODE, 30);

            mMBInterstitalVideoHandler.load(); //load ad

            if (mMBInterstitalVideoHandler.isReady()) {
                // Use this method to determine if the video asset is ready for playback. It is recommended to display the ad only when it's playable.
                mMBInterstitalVideoHandler.show(); //show ad
            }

        }
    }

    public static void showRewardVideo(Activity activity){
        if(showAds && useMintegral){
            String TAG = "REWARDED VIDEO INIT";
            MBRewardVideoHandler mMBRewardVideoHandler = new MBRewardVideoHandler(activity, mintegralRewardPlacementID, mintegralRewardUnitID);
            mMBRewardVideoHandler.setRewardVideoListener(new RewardVideoListener() {
                @Override
                public void onVideoLoadSuccess(MBridgeIds mBridgeIds) {
                    Log.d(TAG, "onVideoLoadSuccess: " + (TextUtils.isEmpty(mBridgeIds.getPlacementId()) ? "" : mBridgeIds.getPlacementId()) + "  " + mBridgeIds.getUnitId());

                }

                @Override
                public void onLoadSuccess(MBridgeIds mBridgeIds) {
                    Log.d(TAG, "onLoadSuccess: " + (TextUtils.isEmpty(mBridgeIds.getPlacementId()) ? "" : mBridgeIds.getPlacementId()) + "  " + mBridgeIds.getUnitId());
                }

                @Override
                public void onVideoLoadFail(MBridgeIds mBridgeIds, String s) {
                    Log.e(TAG, "onVideoLoadFail: "+ s+" " + (TextUtils.isEmpty(mBridgeIds.getPlacementId()) ? "" : mBridgeIds.getPlacementId()) + "  " + mBridgeIds.getUnitId());

                }

                @Override
                public void onAdShow(MBridgeIds mBridgeIds) {
                    Log.d(TAG, "onAdShow: " + (TextUtils.isEmpty(mBridgeIds.getPlacementId()) ? "" : mBridgeIds.getPlacementId()) + "  " + mBridgeIds.getUnitId());

                }

                @Override
                public void onAdClose(MBridgeIds mBridgeIds, RewardInfo rewardInfo) {
                    // Called when the ad is closed
                    Log.d(TAG, "onAdClose: " + mBridgeIds.toString() + rewardInfo.toString());
                    // If 'rewardInfo.isCompleteView()' returns true, it means that the user can be rewarded.
                    if(rewardInfo.isCompleteView()){
                        Toast.makeText(activity,"onADClose:" + rewardInfo.isCompleteView() + ",RewardName:"+rewardInfo.getRewardName() + ",RewardAmout:" + rewardInfo.getRewardAmount(),Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(activity,"onADClose:" + rewardInfo.isCompleteView() + ",RewardName:"+rewardInfo.getRewardName() + ",RewardAmout:" + rewardInfo.getRewardAmount(),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onShowFail(MBridgeIds mBridgeIds, String s) {
                    Log.e(TAG, "onShowFail: " + s + " " + mBridgeIds.toString());

                }

                @Override
                public void onVideoAdClicked(MBridgeIds mBridgeIds) {
                    Log.d(TAG, "onVideoAdClicked : " + (TextUtils.isEmpty(mBridgeIds.getPlacementId()) ? "" : mBridgeIds.getPlacementId()) + "  " + mBridgeIds.getUnitId());

                }

                @Override
                public void onVideoComplete(MBridgeIds mBridgeIds) {
                    Log.d(TAG, "onVideoComplete : " + (TextUtils.isEmpty(mBridgeIds.getPlacementId()) ? "" : mBridgeIds.getPlacementId()) + "  " + mBridgeIds.getUnitId());

                }

                @Override
                public void onEndcardShow(MBridgeIds mBridgeIds) {
                    Log.d(TAG, "onEndcardShow : " + (TextUtils.isEmpty(mBridgeIds.getPlacementId()) ? "" : mBridgeIds.getPlacementId()) + "  " + mBridgeIds.getUnitId());

                }
            });

            mMBRewardVideoHandler.playVideoMute(MBridgeConstans.REWARD_VIDEO_PLAY_MUTE);
            //  mMBRewardVideoHandler.setRewardPlus(true);
            //load
            mMBRewardVideoHandler.load();
            //Display the ad after checking if it's ready.
            if (mMBRewardVideoHandler.isReady()) {
                mMBRewardVideoHandler.show(); // Client reward callback without passing parameters
            }
        }
    }

}
