package app;

public class SimilarityUtil {

    public static int LENGTH_DIF_RANGE = 3;
    public static int CODE_SIMILARITY_RANGE = 3;
    public static double SIMILARITY_FACTOR = 0.70;
    private static String [] keyboardBlocks = {
        "123qwe", "qweasd", "asdzcx ",
        "234wer", "wersdf", "sdfxcv ",
        "345ert", "ertdfg", "dfgcvb ",
        "456rty", "rtyfgh", "fghvbn ",
        "567tyu", "tyughj", "ghjbnm ",
        "678yui", "yuihjk", "hjknm,< ",
        "789uio", "uiojkl", "jklm,.<>",
        "890iop", "iopkl;:", "kl;,./?><'",
        "-=_+\\\'\""
    };
    private static String [] similarFormBlocks = {
      "ilj", "mn", "sc", "wvu", "uy", "o0q", "kc", "qg"
    };

    public static boolean isSimilar(char [] s1, char [] s2){
        char[] str1, str2;
        int dif = s1.length - s2.length;
        if(Math.abs(dif) > LENGTH_DIF_RANGE) return false;
        if(dif <= 0){
            str1 = s1; str2 = s2;
        }else{
            str1 = s2; str2 = s1;
        }
        //criteria
        int ne = 0; int ns1 = 0; int ns2 = 0;  int ns3 = 0; int nn = 0;

        for(int i = 0; i < str1.length; i++){
            //equal symbols
            if(str1[i] == str2[i]){
                ne++;
            }else{
                //absent
                int c = 0;
                for(int j = 0; j < str1.length; j++){
                    if(str2[j] == str1[i]) c++;
                }
                if(c == 0) nn++;
            }
            if(areSimilarByForm(str1[i], str2[i])) ns1++;
            if(Math.abs(str1[i] - str2[i]) <= CODE_SIMILARITY_RANGE) ns2++;
            if(areNearOnBoard(str1[i], str2[i])) ns3++;
        }

        //relational factors
        double nerf = (double) ne/str1.length;
        double ns1rf = (double) ns1/str1.length;
        double ns2rf = (double) ns2/str1.length;
        double ns3rf = (double) ns3/ str1.length;
        double nnrf = (double) nn/str1.length;
        double rf = (nerf + ns1rf + ns2rf + ns3rf)/4 - nnrf;
        return rf >= SIMILARITY_FACTOR;
    }

    private static boolean areSimilarByForm(Character ch1, Character ch2){
        for(String str : similarFormBlocks){
            if(str.contains(ch1.toString().toLowerCase())
                    && str.contains(ch2.toString().toLowerCase())){
                return true;
            }
        }
        return false;
    }

    private static boolean areNearOnBoard(Character ch1, Character ch2){
        for(String str : keyboardBlocks){
            if(str.contains(ch1.toString().toLowerCase())
                    && str.contains(ch2.toString().toLowerCase())) return true;
        }
        return false;
    }
}
