package com.capstone.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.capstone.server.dto.ChatGPTRequest;
import com.capstone.server.dto.ChatGPTResponse;
import com.capstone.server.model.enums.BagType;
import com.capstone.server.model.enums.BottomType;
import com.capstone.server.model.enums.Color;
import com.capstone.server.model.enums.Gender;
import com.capstone.server.model.enums.HairStyle;
import com.capstone.server.model.enums.TopType;

@Service
public class ChatGPTService {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    // TODO : 파라미터 추가
    // 아래 함수 사용
    public String translateEnglishToKorean(Integer age, String genderExceptAge, String hair, String topColor, String topType,
                                        String bottomColor, String bottomType, String bag) {

        String text = this.createUserText(age,
                                        Gender.fromKor(genderExceptAge).getValue(),
                                        HairStyle.fromKor(hair).getValue(),
                                        Color.fromKor(topColor).getValue(),
                                        TopType.fromKor(topType).getValue(),
                                        Color.fromKor(bottomColor).getValue(),
                                        BottomType.fromKor(bottomType).getValue(),
                                        BagType.fromKor(bag).getValue());

        ChatGPTRequest request = new ChatGPTRequest(model, text, "user");
        request.setPresencePenalty(0.9);
        request.addMessages(this.createSystemPrompt(), "system");

        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponse.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }

    // ex) A boy has long hair. He wearing a red short sleeve and a blue a short pants. He is carrying a backpack.
    public String createUserText(Integer age, String genderExceptAge, String hair, String topColor, String topType,
                              String bottomColor, String bottomType, String bag) {
        genderExceptAge = genderExceptAge.toLowerCase();
        String gender;
        String genderIncludeAge;
        String query;

        // 성별 설정
        if (genderExceptAge.equals("man")) {
            gender = "He";
            if (age < 15) {
                genderIncludeAge = "A man";
            } else {
                genderIncludeAge = "A boy";
            }
        } else {
            gender = "She";
            if (age < 15) {
                genderIncludeAge = "A woman";
            } else {
                genderIncludeAge = "A girl";
            }
        }

        // 머리 스타일 정보 추가 
        query = String.format("%s has %s. ", genderIncludeAge, hair);

        // 인상착의 정보 확인
        if (topType.equals("none") && bottomType.equals("none")) {
            throw new IllegalArgumentException("인상착의 정보가 존재하지 않습니다.");
        }

        // 상하의 색상 정보 없는 경우
        else if (topColor.equals("none") && bottomColor.equals("none")) {
            query += String.format("%s wearing a %s and a %s. ",
                                gender, topType, bottomType.equals("skirt") ? bottomType : "a " + bottomType);
        }

        // 상하의 정보 중 단 하나를 알고 있으며, 색상 정보를 제외하고 종류만 알고 있는 경우
        else if (topColor.equals("none") && bottomType.equals("none")) {
            query += String.format("%s wearing a %s. ", gender, topType);
        } else if (bottomColor.equals("none") && topType.equals("none")) {
            query += String.format("%s wearing %s. ", gender, bottomType.equals("skirt") ? bottomType : "a " + bottomType);
        }

        // 상하의 정보 중 하나의 색상 정보만 누락된 경우
        else if (topColor.equals("none")) {
            query += String.format("%s wearing a %s %s and %s %s. ",
                                gender, bottomColor, bottomType, gender, bag.equals("backpack") ? "is carrying" : "is holding a " + bag);
        } else if (bottomColor.equals("none")) {
            query += String.format("%s wearing a %s %s and %s %s. ",
                                gender, topColor, topType, gender, bag.equals("backpack") ? "is carrying" : "is holding a " + bag);
        }

        // 상하의 정보 중 하나의 종류 정보가 누락된 경우
        else if (topType.equals("none")) {
            query += String.format("%s wearing %s %s. ", gender, bottomColor, bottomType);
        } else if (bottomType.equals("none")) {
            query += String.format("%s wearing %s %s. ", gender, topColor, topType);
        }

        // 모든 정보가 포함된 경우
        else {
            query += String.format("%s wearing a %s %s and a %s %s. ",
                                gender, topColor, topType, bottomColor, bottomType.equals("skirt") ? bottomType : "a " + bottomType);
        }

        // 가방 정보 추가
        if (bag.equals("backpack")) {
            query += String.format("%s is carrying a %s.", gender, bag);
        } else if (bag.equals("bag")) {
            query += String.format("%s is holding a %s.", gender, bag);
        } 

        
        return query.replace("_", " ");
    }

    public String createSystemPrompt() {
        String prompt = """
            You are a translator.
            I will provide you with the information of the character's description in English, so please translate this information into Korean correctly.

            An example is as follows.

            Case 1.
            Input sentence: A woman has long hair. She wearing a blue long sleeve shirt and white short pants. She is holding a bag.
            Desired Translation: 긴 머리. 파란색 긴팔 셔츠와 흰 반바지를 입고, 가방을 들고 있음.

            Case 2.
            Input sentence: A boy has long hair. He wearing a maroon long sleeve shirt and black long pants. 
            Desired Translation: 긴 머리. 자주색 긴팔 셔츠와 검정 긴 바지를 입고 있음.

            Case 3.
            Input sentence: A girl has short hair. She wearing a red long sleeve shirt and a dark blue skirt. She is carrying a backpack.
            Desired Translation: 짧은 머리. 빨간 긴팔 셔츠와 남색 치마를 입고, 가방을 메고 있음.

            Case 4.
            Input sentence: A man has short hair. He wearing a orange winter coat and blue long pants. He is carrying a backpack.
            Desired Translation: 짧은 머리. 주황색 패딩과 파란색 긴 바지를 입고, 가방을 메고 있음.

            Case 5.
            Input sentence: A girl has short hair. She wearing a purple short sleeve shirt and pink short pants. She is holding a backpack.
            Desired Translation: 짧은 머리. 보라색 반팔 셔츠와 핑크색 반바지를 입고, 가방을 들고 있음.

            Case 6.
            Input sentence: A man has short hair. He wearing a coat and long pants.
            Desired Translation: 짧은 머리. 코트와 긴 바지를 입고 있음.

            Case 7.
            Input sentence: A boy has long hair. He wearing a winter coat. He is carrying a backpack.
            Desired Translation: 긴 머리. 패딩을 입고, 가방을 메고 있음.

            Case 8.
            Input sentence: A woman has short hair. She wearing a skirt. She is holding a backpack.
            Desired Translation: 짧은 머리. 치마를 입고, 가방을 들고 있음.

            Case 9.
            Input sentence: A man has long hair. He wearing a coat and khaki short pants. 
            Desired Translation: 긴 머리. 코트와 카키색 반바지를 입고 있음.

            Case 10.
            Input sentence: A girl has short hair. She wearing a light blue winter coat and a skirt.
            Desired Translation: 짧은 머리. 하늘색 패딩과 치마를 입고 있음.

            Case 11.
            Input sentence: A boy has short hair. He wearing dark blue short pants.
            Desired Translation: 짧은 머리. 남색 반바지를 입고 있음.

            Case 12.
            Input sentence: A woman has short hair. She wearing a beige coat. She is holding a backpack."
            Desired Translation: 짧은 머리. 베이지색 코트를 입고, 가방을 들고 있음.

            Please respond to every sentence simply.
            """;
        return prompt;
    }

}
