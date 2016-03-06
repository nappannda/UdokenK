package com.sakasho.udokenk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;

/*
 栄養素を統括するクラス
 栄養素をCSVから取得し配列に保存し他のクラスからのデータ取得に対応する
 */
public class NutritionsUtil {
    private String[][] nutritions_data = new String[150][50];//すべての栄養素データ
    private String[][] nutritions_average_data = new String[22][10];//各年代性別の一日取得平均栄養素データ
    private String[][] calorie_consumption_data = new String[35][30];//各運動ごとの消費栄養素データ
    private Double[][] okazu_data = new Double[36][7];//おかずのデータ保持
    private Double[] okazu_total = new Double[36];
    private int[] okazu_ranking = new int[36];//おかずの栄養素のランキング保持
    private AssetManager assetManager;
    private InputStream inputStream = null;
    private BufferedReader bufferedReader = null;
    private SharedPreferences sp;
    private int[] sb = {29, 32, 36, 30, 35, 31, 33, 27, 34, 28, 23, 21, 18,
            15, 20, 16, 19, 17, 22, 24, 40, 39, 42, 41};//トッピングのおかずの栄養素がどこにあるかを格納
    private Context context;
    private int[] consumption_year = {4, 12, 19, 26, 26, 26};//消費するカロリーがどこにあるかを格納

    public NutritionsUtil(Context context) {
        this.context = context;
        assetManager = context.getResources().getAssets();
        SetNutritionsData();
        SetNutritionsAverageData();
        SetCalorieConsumptionData();
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Double getConsumptionCalorie(int spot_number) {
        int gender_number;
        if (sp.getBoolean("setting_gender", true)) {
            gender_number = 1;
        } else {
            gender_number = 4;
        }
        return Double.valueOf(calorie_consumption_data[consumption_year[sp.getInt("setting_year", 0)]][gender_number + spot_number * 6]);
    }

    /*
        うどんの栄養素を返す関数
        うどんの番号を受け取りDouble型の配列で返す
     */
    public Double[] getUdonNutritions(int udon_number, int size) {
        Double udon_size[] = {0.7, 1.0, 1.3};
        Double udonNutritions[] = new Double[7];
        for (int i = 0; i < 7; i++) {
            BigDecimal u1 = new BigDecimal(udon_size[size - 1]);
            BigDecimal u2 = new BigDecimal(Double.valueOf(nutritions_data[udon_number][i + 1]));
            udonNutritions[i] = u1.multiply(u2).doubleValue();//誤差が出にくいようにBigDecimalで計算
        }
        return udonNutritions;
    }

    public Double[] getToppingNutritions(ArrayList<Integer> toppings) {
        Double toppingNutritions[] = new Double[7];
        for (int i = 0; i < 7; i++) {
            toppingNutritions[i] = 0.0;
        }
        for (int topping : toppings) {
            for (int i = 0; i < 7; i++) {
                BigDecimal t1 = new BigDecimal(toppingNutritions[i]);
                BigDecimal t2 = new BigDecimal(Double.valueOf(nutritions_data[sb[topping]][i + 1]));
                toppingNutritions[i] = t1.add(t2).doubleValue();//上記と同じでBigDecimalで計算
            }
        }
        return toppingNutritions;
    }

    public Double[] getOkazuNutritions(ArrayList<Integer> okazus) {
        Double okazuNutritions[] = new Double[7];
        for (int i = 0; i < 7; i++) {
            okazuNutritions[i] = 0.0;
        }
        for (int okazu : okazus) {
            for (int i = 0; i < 7; i++) {
                BigDecimal t1 = new BigDecimal(okazuNutritions[i]);
                BigDecimal t2 = new BigDecimal(Double.valueOf(nutritions_data[45 + okazu][i + 1]));
                okazuNutritions[i] = t1.add(t2).doubleValue();//上記と同じでBigDecimalで計算
            }
        }
        return okazuNutritions;
    }

    public int getAwaseNutritionPoint(Double[] nutritions) {
        int genderNumber;

        if (sp.getBoolean("setting_gender", true)) {//男性選択
            genderNumber = 2;
        } else {
            genderNumber = 7;
        }
        int total_point = 0;
        for (int i = 0; i < 7; i++) {
            BigDecimal average = new BigDecimal(Double.valueOf(nutritions_average_data[genderNumber + 10
                    + sp.getInt("setting_year", 0) / 2][i + 2]));
            BigDecimal nutrition = new BigDecimal(nutritions[i]);
            BigDecimal percent = nutrition.divide(average, 1, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).setScale(1, BigDecimal.ROUND_HALF_UP);
            percent = percent.subtract(new BigDecimal(100)).abs();
            int point = percent.divide(new BigDecimal(16)).setScale(1, BigDecimal.ROUND_HALF_UP).intValue();
            if (point > 5) {
                point = 5;
            }
            total_point += point;
        }
        return total_point / 7;
    }


    /*
        栄養素が不足しているか取り過ぎているかを判断するための数値を取得
     */
    public double getJudgeNutritionNumber(int i) {
        int genderNumber;

        if (sp.getBoolean("setting_gender", true)) {//男性選択
            genderNumber = 2;
        } else {
            genderNumber = 7;
        }
        BigDecimal average = new BigDecimal(Double.valueOf(nutritions_average_data[genderNumber + 10
                + sp.getInt("setting_year", 0) / 2][i + 2]));
        BigDecimal b1 = new BigDecimal(1.1);
        BigDecimal b2 = new BigDecimal(3);
        Double judgeNumber = average.multiply(b1).divide(b2).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();// 計算 average*1.1/3 小数点第二位で四捨五入

        return judgeNumber;
    }


    private void SetNutritionsData() {
        try {
            inputStream = assetManager.open("natritions.csv");
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            int n = 0;
            while ((line = bufferedReader.readLine()) != null) {
                String[] str = line.split(",");
                for (int i = 0; i < str.length; i++) {
                    nutritions_data[n][i] = str[i];
                }
                n = n + 1;
                for (int j = 0; j < str.length; j++) {
                    str[j] = null;

                }
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    private void SetNutritionsAverageData() {
        try {
            inputStream = assetManager.open("average_natritions.csv");
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line1 = null;
            int b = 0;
            while ((line1 = bufferedReader.readLine()) != null) {
                String[] str1 = line1.split(",");
                for (int m = 0; m < str1.length; m++) {
                    nutritions_average_data[b][m] = str1[m];
                }
                b = b + 1;
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
    }

    private void SetCalorieConsumptionData() {
        try {
            inputStream = assetManager.open("calorie_consumption.csv");
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line2 = null;
            int c = 0;
            while ((line2 = bufferedReader.readLine()) != null) {
                String[] str2 = line2.split(",");
                for (int m = 0; m < str2.length; m++) {
                    calorie_consumption_data[c][m] = str2[m];
                }
                c = c + 1;
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }

    }

    public int getUdonFitOkazu(Double[] udon_nutritions) {
        int genderNumber;

        if (sp.getBoolean("setting_gender", true)) {//男性選択
            genderNumber = 2;
        } else {
            genderNumber = 7;
        }
        int o = 0;
        for (int i = 45; i <= 72; i++) {
            for (int j = 0; j < 7; j++) {
                BigDecimal udon_nutrition = new BigDecimal(udon_nutritions[j]);
                BigDecimal okazu_nutrition = new BigDecimal(Double.parseDouble(nutritions_data[i][j + 1]));
                okazu_data[o][j] = udon_nutrition.add(okazu_nutrition).doubleValue();
            }
            ++o;
        }
        for (int i = 75; i <= 82; i++) {
            for (int j = 0; j < 7; j++) {
                BigDecimal udon_nutrition = new BigDecimal(udon_nutritions[j]);
                BigDecimal okazu_nutrition = new BigDecimal(Double.parseDouble(nutritions_data[i][j + 1]));
                okazu_data[o][j] = udon_nutrition.add(okazu_nutrition).doubleValue();
            }
            ++o;
        }

        for (int i = 0; i < 36; i++) {
            okazu_total[i] = 0.0;
            for (int j = 0; j < 7; j++) {
                BigDecimal average = new BigDecimal(Double.valueOf(nutritions_average_data[genderNumber + 10
                        + sp.getInt("setting_year", 0) / 2][j + 2]));
                BigDecimal okazu_nutrition = new BigDecimal(okazu_data[i][j]);
                okazu_data[i][j] = okazu_nutrition.divide(average, 1, BigDecimal.ROUND_HALF_UP).abs().doubleValue();
                okazu_total[i] += okazu_data[i][j];
            }
            okazu_ranking[i] = 1;//初期化
        }
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 36; j++) {
                if (okazu_total[i] > okazu_total[j]) {
                    okazu_ranking[i]++;
                }
            }
        }
        int random = (int) (Math.random() * 5);
        int recommend = 0;
        for (int i = 0; i < 36; i++) {
            if (okazu_ranking[i] == random) {
                recommend = i;
            }
        }
        return recommend;
    }


}
