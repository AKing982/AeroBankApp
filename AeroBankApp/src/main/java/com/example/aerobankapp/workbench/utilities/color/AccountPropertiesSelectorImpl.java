package com.example.aerobankapp.workbench.utilities.color;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class AccountPropertiesSelectorImpl implements AccountPropertiesSelector
{
    private String[] colorList;
    private String[] imageList;

    public AccountPropertiesSelectorImpl(){
        initializeColorList();
        initializeImageURLList();;
    }

    void initializeColorList(){
        colorList = new String[]{"RED", "PURPLE", "TEAL", "BLUE", "GREY", "TAN", "BEIGE"};
    }

    void initializeImageURLList(){
        imageList = new String[]{"/images/pexels-pixabay-417173.jpg",
                "/images/pexels-archie-binamira-672451.jpg",
                "/images/pexels-james-wheeler-417074.jpg",
                "/images/pexels-krivec-ales-547115.jpg"};
    }

    @Override
    public String selectRandomAccountColor() {
        Random random = getRandom();
        int index = random.nextInt(colorList.length);
        return colorList[index];
    }

    @Override
    public String selectRandomImageURL() {
        Random random = getRandom();
        int index = random.nextInt(imageList.length);
        return imageList[index];
    }

    private Random getRandom(){
        return new Random();
    }
}
