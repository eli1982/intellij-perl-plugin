package com.intellij.perlplugin.bo;

import com.intellij.perlplugin.ModulesContainer;

import java.util.ArrayList;

/**
 * Created by eli on 28-11-14.
 */
public class Sub {
    private Package packageObj;
    private String subName;
    private int positionInFile;
    private ArrayList<Argument> arguments = new ArrayList<Argument>();

    public Sub(Package packageObj, String subName) {
        this.subName = subName;
        this.packageObj = packageObj;
        ModulesContainer.addSub(this);
    }

    public String getName() {
        return subName;
    }

    public void setName(String subName) {
        this.subName = subName;
    }

    public ArrayList<Argument> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<Argument> arguments) {
        this.arguments = arguments;
    }

    public int getPositionInFile() {
        return positionInFile;
    }

    public void setPositionInFile(int positionInFile) {
        this.positionInFile = positionInFile;
    }

    public Package getPackageObj() {
        return packageObj;
    }

    @Override
    public String toString() {
        return "Sub{" + "\n" +
                "   subName='" + subName + '\'' + ",\n" +
                "   packageObj='" + packageObj.getPackageName() + '\'' + ",\n" +
                "   positionInFile='" + positionInFile + '\'' + ",\n" +
                "   arguments=" + arguments + "\n" +
                '}';
    }

    public String toString2() {
        StringBuffer sb = new StringBuffer();
        sb.append(getName());
        sb.append("(");
        for (int i = 0; i < getArguments().size(); i++) {
            Argument argument = getArguments().get(i);
            sb.append(" " + argument.toString2());
            if (i < getArguments().size() - 1) {
                sb.append(",");
            }
        }


        if (getArguments().size() == 0) {
            sb.append(")");
        } else {
            sb.append(" )");
        }

        return sb.toString();
    }
}
