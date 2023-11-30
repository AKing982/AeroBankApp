package com.example.aerobankapp.workbench.passwordSkin;

import javafx.scene.control.PasswordField;
import javafx.scene.control.skin.TextFieldSkin;

public class ShowPasswordField extends TextFieldSkin
{
    public ShowPasswordField(PasswordField textField)
    {
        super(textField);
    }

    @Override
    protected double computePrefWidth(double v, double v1, double v2, double v3, double v4)
    {
        double prefWidth = super.computePrefWidth(v, v1, v2, v3, v4);
        return super.computePrefWidth(v, v1, v2, v3, v4);
    }

    @Override
    protected double computeMinHeight(double v, double v1, double v2, double v3, double v4)
    {
        double minHeight = super.computeMinHeight(v, v1, v2, v3, v4);
        return super.computeMinHeight(v, v1, v2, v3, v4);
    }

    @Override
    protected double computePrefHeight(double v, double v1, double v2, double v3, double v4)
    {
        return super.computePrefHeight(v, v1, v2, v3, v4);
    }

    @Override
    protected double computeMaxHeight(double v, double v1, double v2, double v3, double v4)
    {
        return super.computeMaxHeight(v, v1, v2, v3, v4);
    }

    @Override
    public void replaceText(int i, int i1, String s)
    {
        super.replaceText(i, i1, s);
    }

    @Override
    protected String maskText(String s)
    {
        return super.maskText(s);
    }
}
