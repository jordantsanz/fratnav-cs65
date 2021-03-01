package com.example.fratnav.tools;

import com.example.fratnav.databaseHelpers.HouseDatabaseHelper;
import com.example.fratnav.models.House;

import java.util.ArrayList;


public class HouseCreation {


    public ArrayList<House> houses;

    public void createHouses() {
        //Fraternities
        House AlphaChi = new House("Alpha Chi Alpha", 0, "",1919, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House Beta = new House("Beta Alpha Omega", 0, "",  1858, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House BonesGate = new House("Bones Gate", 0, "", 1901, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House ChiGam = new House("Chi Gam Epsilon", 0, "", 1905, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House Herot = new House ("Chi Hereot", 0, "", 1898, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House GDX = new House("Gamma Delta Chi", 0, "", 1908, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House TriKap = new House ("Kappa Kappa Kappa", 0, "", 1842, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House PhiDelt = new House("Phi Delta Alpha", 0, "", 1884, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House PsiU = new House ("Psi Upsilon", 0, "", 1841, true , new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House SigNu = new House ("Sigma Nu", 0, "", 1901,true,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House TDX = new House("Theta Delta Chi", 0, "",  1869, true, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House Zete = new House( "Zeta PSi",0, "",  1853, true,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        //Sororities
        House APhi = new House("Alpha Phi", 0, "", 2006, true, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House AZD = new House("Alpha Xi Delta",0, "", 1997 ,true,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House ChiDelt = new House("Chi Delta", 0,"", 1984, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House EKT = new House("Epsilon Kapppa Theta", 0, "", 1981, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House KD = new House("Kappa Delta",0, "", 2009, true,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House KDE = new House("Kappa Delta Epsilon", 0, "", 1980, false,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House Kappa = new House("Kappa Kappa Gamma", 0, "",  1976, true,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House SigDelt = new House("Sigma Delta", 0, "", 1976, false , new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        //Gender Inclusive
        House AlphaTheta = new House("Alpha Theta", 0, "", 1921, false,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House phiTau = new House("Phi Tau", 0, "", 1905, false,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House Tabard = new House("The Tabard", 0, "", 1857, false,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        //National Pan Helic Council
        House APA = new House("Alpha Phi Alpha Fraternity, Inc.", 0, "", 1972, true, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House AKA = new House("Alpha Kappa Alpha Sorority, Inc",0, "", 1983, true, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());
        House Deltas = new House("Delta Sigma Theta Sorority, Inc",0,"",1935, true, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>());

        houses = new ArrayList<>();
        houses.add(AlphaChi);
        houses.add(Beta);
        houses.add(BonesGate);
        houses.add(ChiGam);
        houses.add(Herot);
        houses.add(GDX);
        houses.add(TriKap);
        houses.add(PhiDelt);
        houses.add(PsiU);
        houses.add(SigNu);
        houses.add(TDX);
        houses.add(Zete);
        houses.add(APhi);
        houses.add(AZD);
        houses.add(ChiDelt);
        houses.add(EKT);
        houses.add(KD);
        houses.add(KDE);
        houses.add(Kappa);
        houses.add(SigDelt);
        houses.add(AlphaTheta);
        houses.add(phiTau);
        houses.add(Tabard);
        houses.add(APA);
        houses.add(AKA);
        houses.add(Deltas);
        for (int i = 0; i < houses.size(); i++) {
            HouseDatabaseHelper.createHouse(houses.get(i));
        }
    }

}
