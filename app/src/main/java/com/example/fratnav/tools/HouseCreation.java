package com.example.fratnav.tools;

import com.example.fratnav.R;
import com.example.fratnav.databaseHelpers.HouseDatabaseHelper;
import com.example.fratnav.models.House;

import java.util.ArrayList;
import java.util.HashMap;


public class HouseCreation {


    public ArrayList<House> houses;

    public void createHouses() {
//        // Fraternities
//        House AlphaChi = new House("Alpha Chi", 0, "",1919, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.ic_axa);
//        House Beta = new House("Beta", 0, "",  1858, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.beta);
//        House BonesGate = new House("BG", 0, "", 1901, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.bg);
//        House ChiGam = new House("Chi Gam", 0, "", 1905, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.chigam);
//        House Herot = new House ("Hereot", 0, "", 1898, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.heorot);
//        House GDX = new House("GDX", 0, "", 1908, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.gdx);
//        House TriKap = new House ("Tri-Kap", 0, "", 1842, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.trikap);
//        House PhiDelt = new House("Phi Delt", 0, "", 1884, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.phidelta);
//        House PsiU = new House ("Psi U", 0, "", 1841, true , new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.psiu);
//
//        House TDX = new House("TDX", 0, "",  1869, true, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.tdx);
//        House Zete = new House( "Zete",0, "",  1853, true,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.zetapsi);
//        //Sororities
//        House APhi = new House("APhi", 0, "", 2006, true, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.aphi1);
//        House AZD = new House("AXiD",0, "", 1997 ,true,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.axid);
//        House ChiDelt = new House("Chi Delt", 0,"", 1984, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.chidelt);
//        House EKT = new House("EKT", 0, "", 1981, false, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.ekt);
//        House KD = new House("KD",0, "", 2009, true,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.kd);
//        House KDE = new House("KDE", 0, "", 1980, false,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.kde);
//        House Kappa = new House("Kappa", 0, "",  1976, true,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.kkg);
//        House SigDelt = new House("Sigma Delt", 0, "", 1976, false , new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.sigdelt);
//        //Gender Inclusive
//        House AlphaTheta = new House("Alpha Theta", 0, "", 1921, false,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.alphatheta);
//        House phiTau = new House("Phi Tau", 0, "", 1905, false,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.phitau);
//        House Tabard = new House("Tabard", 0, "", 1857, false,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.tabard);
//        //National Pan Helic Council
//        House APA = new House("Alphas", 0, "", 1972, true, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.alphas);
//        House AKA = new House("AKA",0, "", 1983, true, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.aka);
//        House Deltas = new House("Deltas",0,"",1935, true, new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new ArrayList<>(), R.drawable.deltas);
//
//        houses = new ArrayList<>();
//        houses.add(AlphaChi);
//        houses.add(Beta);
//        houses.add(BonesGate);
//        houses.add(ChiGam);
//        houses.add(Herot);
//        houses.add(GDX);
//        houses.add(TriKap);
//        houses.add(PhiDelt);
//        houses.add(PsiU);
////        houses.add(SigNu);
//        houses.add(TDX);
//        houses.add(Zete);
//        houses.add(APhi);
//        houses.add(AZD);
//        houses.add(ChiDelt);
//        houses.add(EKT);
//        houses.add(KD);
//        houses.add(KDE);
//        houses.add(Kappa);
//        houses.add(SigDelt);
//        houses.add(AlphaTheta);
//        houses.add(phiTau);
//        houses.add(Tabard);
//        houses.add(APA);
//        houses.add(AKA);
//        houses.add(Deltas);
//        for (int i = 0; i < houses.size(); i++) {
//            HouseDatabaseHelper.createHouse(houses.get(i));
//        }
    }


    public static void createHouse(String username, String id){
        switch(username){
            case "signu":
                House SigNu = new House ("Sig Nu", 0, "", 1901,true,  new ArrayList<>(),  new ArrayList<>(),new ArrayList<>(), "", new HashMap<>(), R.drawable.signu1, id);
                HouseDatabaseHelper.createHouse(SigNu);
                break;
        }
    }

}
