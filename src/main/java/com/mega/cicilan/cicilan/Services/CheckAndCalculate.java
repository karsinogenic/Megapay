package com.mega.cicilan.cicilan.Services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mega.cicilan.cicilan.Models.MapInput;
import com.mega.cicilan.cicilan.Models.Products;
import com.mega.cicilan.cicilan.Models.Rules;
import com.mega.cicilan.cicilan.Repositories.ChannelRepository;
import com.mega.cicilan.cicilan.Repositories.ProductsRepository;
import com.mega.cicilan.cicilan.Repositories.RequestRepository;
import com.mega.cicilan.cicilan.Repositories.RulesRepository;

public class CheckAndCalculate {
    private MapInput input;
    private Map inputMap;
    private ChannelRepository channelRepository;
    private ProductsRepository productsRepository;
    private RequestRepository requestRepository;
    private RulesRepository rulesRepository;

    public CheckAndCalculate(MapInput input, Map inputMap, ChannelRepository channelRepository,
            ProductsRepository productsRepository,
            RulesRepository rulesRepository, RequestRepository requestRepository) {
        this.input = input;
        this.inputMap = inputMap;
        this.channelRepository = channelRepository;
        this.requestRepository = requestRepository;
        this.rulesRepository = rulesRepository;
        this.requestRepository = requestRepository;
        this.productsRepository = productsRepository;
    }

    public Map run() {
        String hasilCek = this.check();
        Map hasilCalculate = this.calculate(hasilCek);
        return hasilCalculate;
    }

    public String check() {
        // System.out.println("map input: " + inputMap);
        String planCode = "";
        String nama_channel = this.channelRepository.getNamaChannel(input.getChannelCode());
        // System.out.println(nama_channel);
        if (nama_channel.isEmpty()) {
            return "TAI";
        }
        List<String> listPlan = this.rulesRepository.findDistinctByKode(input.getChannelCode());
        listPlan = listPlan.stream()
                .sorted() // Sorts in ascending order
                .collect(Collectors.toList());

        List<String> listHasilError = new ArrayList<>();
        List<String> listError = new ArrayList<>();
        List<String> listHasil = new ArrayList<>();

        for (String plan : listPlan) {
            List<Rules> listRule = this.rulesRepository.findByTier_code(plan);
            int i = 1;
            for (Rules rule : listRule) {
                String triggerParam = this.cekRule(rule);
                if (!triggerParam.isEmpty()) {
                    listHasilError.add(plan);
                    listError.add(triggerParam);
                    break;
                }
                if (i == listRule.size() && triggerParam.isEmpty()) {
                    listHasil.add(plan);
                }
                i++;
            }
        }
        System.out.println("------------------------------------");
        System.out.println(listHasilError.toString());
        System.out.println(listError.toString());
        System.out.println(listHasil.toString());
        System.out.println("---------------END------------------");

        // for(int i=0;i<listHasil.size();i++){
        // System.out.println(listHasil.get(i));
        // }
        if (listHasil.size() > 0) {
            planCode = listHasil.get(0);
        }

        return planCode;
    }

    public Map calculate(String tier_code) {

        List<Products> products = this.productsRepository.findProductByTierCode(tier_code);
        Map<String, Object> mapBaru = new HashMap<String, Object>();
        JSONArray jsonArray = new JSONArray();
        for (Products products2 : products) {
            JSONObject jsonObject = new JSONObject();
            // System.out.println(products2.getProduct_name());
            Long new_amount = Long.valueOf(0);
            try {
                new_amount = Long.valueOf(input.getAmount());
            } catch (Exception e) {
                System.out.println("amount");
            }
            jsonObject.put("plan_code", products2.getPlan_code());
            Double cicilan = hitungCicilan(new_amount, products2.getInterest(),
                    products2.getInterest_type(),
                    products2.getTenor());
            BigDecimal cicilan1 = new BigDecimal(cicilan).setScale(0,
                    RoundingMode.HALF_UP);

            System.out.println("cicilan: " + Long.valueOf(input.getAmount()) + " : " + cicilan1);
            jsonObject.put("cicilan", cicilan1);
            jsonObject.put("tenor", products2.getTenor());

            Double interestRate;
            if (products2.getInterest_channel().equals("F")) {
                interestRate = hitungInterestRate(cicilan, new_amount, products2.getInterest(),
                        products2.getTenor());
            } else {
                interestRate = products2.getInterest();
            }
            jsonObject.put("interest", interestRate);

            System.out.println("interest: " + Long.valueOf(input.getAmount()) + " : " + interestRate);
            jsonArray.put(jsonObject);
        }
        mapBaru.put("data", jsonArray.toList());
        // mapBaru.put("cicilan", cicilan1);

        return mapBaru;
    }

    public Double hitungCicilan(Long loan, Double oriInterest, String kode, Long tenor) {
        Double hasil;
        Double y = (1 - (Math.pow((1 + (oriInterest / 100 / 12)), -tenor)));
        if (kode.equals("E")) {
            // System.out.println("y=" + y);
            hasil = loan * (oriInterest / 100 / 12) / y;
            // System.out.println("hasil: " + hasil);
        } else {
            hasil = (loan / tenor) + (loan * oriInterest / 100);
        }
        return hasil;
    }

    public Double hitungInterestRate(Double cicilan, Long loan, Double oriInterest, Long tenor) {
        Double hasil = (cicilan - (loan / tenor)) / loan * 100;
        // System.out.println("eRate:" + hasil);
        return hasil;
    }

    public String cekRule(Rules rules) {
        String hasil = "";
        String key_param = rules.getParameter();
        // System.out.println(key_param);
        String value_param = "";
        try {
            // System.out.println(input.get(key_param).getClass());
            value_param = inputMap.get(key_param).toString();
        } catch (Exception e) {
            // response.put("status", key_param + " tidak dimasukan");
            // return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        String operator = rules.getOperator();
        // System.out.println("value param: " + value_param);
        if (value_param != null) {
            switch (operator) {
                case ">=":
                    // System.out.println(Integer.parseInt(value_param) >=
                    // Integer.parseInt(hasil.getValue()));
                    if (Integer.parseInt(value_param) >= Integer.parseInt(rules.getValue()) == false) {
                        // System.out.println(key_param);
                        hasil = key_param;
                        // response.put("cause " + hasil.getKode_tier(),
                        // hasil.getKriteria().getNama_kriteria());

                        // status_kode = false;
                    }
                    break;
                case "<=":
                    // System.out.println(Integer.parseInt(value_param) <=
                    // Integer.parseInt(hasil.getValue()));
                    if (Integer.parseInt(value_param) <= Integer.parseInt(rules.getValue()) == false) {
                        // System.out.println(key_param);
                        hasil = key_param;

                        // response.put("cause " + hasil.getKode_tier(),
                        // hasil.getKriteria().getNama_kriteria());

                        // status_kode = false;
                    }
                    break;
                case ">":
                    // System.out.println(Integer.parseInt(value_param) >
                    // Integer.parseInt(hasil.getValue()));
                    if (Integer.parseInt(value_param) > Integer.parseInt(rules.getValue()) == false) {
                        // System.out.println(key_param);
                        hasil = key_param;

                        // response.put("cause " + hasil.getKode_tier(),
                        // hasil.getKriteria().getNama_kriteria());

                        // status_kode = false;
                    }
                    break;
                case "<":
                    // System.out.println(Integer.parseInt(value_param) <
                    // Integer.parseInt(hasil.getValue()));
                    if (Integer.parseInt(value_param) < Integer.parseInt(rules.getValue()) == false) {
                        // System.out.println(key_param);
                        hasil = key_param;

                        // response.put("cause " + hasil.getKode_tier(),
                        // hasil.getKriteria().getNama_kriteria());

                        // status_kode = false;
                    }
                    break;
                case "=":
                    // System.out.println(value_param.equals(hasil.getValue()));
                    if (value_param.equals(rules.getValue()) == false) {
                        // System.out.println(key_param);
                        hasil = key_param;

                        // response.put("cause " + hasil.getKode_tier(),
                        // hasil.getKriteria().getNama_kriteria());

                        // status_kode = false;
                    }
                    break;
                case "<>":
                    // System.out.println(!value_param.equals(hasil.getValue()));
                    if (!value_param.equals(rules.getValue()) == false) {
                        // System.out.println(key_param);
                        hasil = key_param;

                        // response.put("cause " + hasil.getKode_tier(),
                        // hasil.getKriteria().getNama_kriteria());
                        // status_kode = false;
                    }
                    break;
                case "IN":
                    String[] newArr = rules.getValue().split(",");
                    List<String> newArrList = Arrays.asList(newArr);
                    // for (String string : newArrList) {
                    // System.out.println("list: " + string);
                    // }

                    // System.out.println(value_param);
                    ObjectMapper objectMapper = new ObjectMapper();
                    // value_param = value_param.substring(1, value_param.length() - 1);
                    // System.out.println(value_param);
                    // value_param.replaceAll("]", "");
                    String[] new_value_param = value_param.split(",");
                    JSONArray jsonArray = new JSONArray();
                    List<String> list_value_param = Arrays.asList(new_value_param);

                    if (list_value_param.size() > 0) {
                        for (String value_String : new_value_param) {
                            // System.out.println(key_param);
                            // System.out.println("newArr" + Arrays.toString(newArr) + "value:" +
                            // value_String + ":");
                            if (Arrays.asList(newArr).contains(value_String) == false
                                    && rules.getAnd_or().toLowerCase().equals("and")) {
                                hasil = key_param;

                                // System.out.println(
                                // "nih" + "newArr" + Arrays.toString(newArr) + "value:" + value_String + ":");
                                // status_kode = false;
                                // response.put("cause " + hasil.getKode_tier(),
                                // hasil.getKriteria().getNama_kriteria());
                                break;
                            } else if (Arrays.asList(newArr).contains(value_String) == false
                                    && rules.getAnd_or().toLowerCase().equals("or")) {
                                // status_kode = false;
                                hasil = key_param;

                            } else if (Arrays.asList(newArr).contains(value_String) == true
                                    && rules.getAnd_or().toLowerCase().equals("or")) {
                                // status_kode = true;
                                hasil = "";

                                break;
                            }
                        }
                    } else {
                        hasil = key_param;
                        // status_kode = false;
                    }

                    break;
                case "NOT IN":
                    String[] newArr1 = rules.getValue().split(",");
                    List<String> list_value_param1 = Arrays.asList(newArr1);

                    if (list_value_param1.size() > 0) {
                        // System.out.println(!Arrays.asList(newArr1).contains(value_param));
                        if (!Arrays.asList(newArr1).contains(value_param) == false
                                && rules.getAnd_or().toLowerCase().equals("and")) {
                            // System.out.println(key_param);
                            hasil = key_param;
                            break;
                            // status_kode = false;
                            // response.put("cause " + hasil.getKode_tier(),
                            // hasil.getKriteria().getNama_kriteria());
                        } else if (!Arrays.asList(newArr1).contains(value_param) == false
                                && rules.getAnd_or().toLowerCase().equals("or")) {
                            // status_kode = false;
                            hasil = key_param;

                        } else if (!Arrays.asList(newArr1).contains(value_param) == true
                                && rules.getAnd_or().toLowerCase().equals("or")) {
                            // status_kode = true;
                            hasil = "";
                            break;
                        }
                    } else {
                        // status_kode = false;
                    }
                    break;

            }
        } else {
            // response.put("status", key_param + " tidak dimasukan");
            // return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        return hasil;
    }

}
