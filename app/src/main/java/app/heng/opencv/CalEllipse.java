package app.heng.opencv;

/**
 * Created by Heng on 16/11/2558.
 */
public class CalEllipse {
    public CalEllipse (){}

    // สมการที่ ๘ ยังไม่เขียน ไม่รุ้จะเขียนยังไง

    public static double delta (float a, float b, float c){
        return b * b - a * c;
    }

    public  static  double s (float a, float b, float c){
        return Math.sqrt(1 + (4 * b*b)/((a-c)*(a-c)));
    }

    public static double nom (float a, float b, float c,float d, float f, float g){
        return 2*(a*f*f + c*d*d + g*b*b - 2 * b * d * f - a * c * g);
    }

    public static double a_prime (float a, float b, float c,float d, float f, float g){
        return Math.round(Math.sqrt(CalEllipse.nom(a,b,c,d,f,g) / (CalEllipse.delta(a,b,c) * ((c - a) * CalEllipse.s(a,b,c) - (c + a)))));
    }

    public static double b_prime (float a, float b, float c,float d, float f, float g){
        return Math.round(Math.sqrt(CalEllipse.nom(a,b,c,d,f,g) / (CalEllipse.delta(a,b,c) * ((a - c) * CalEllipse.s(a,b,c) - (c + a)))));
    }

    public static double x0(float a, float b, float c,float d, float f){
        return Math.round((c * d - b * f) / CalEllipse.delta(a,b,c));
    }

    public static double y0(float a, float b, float c,float d, float f){
        return Math.round((a * f - b * d) / CalEllipse.delta(a,b,c));
    }

    public static double rMax(float a, float b, float c,float d, float f, float g){
        return Math.round(Math.max(CalEllipse.a_prime(a,b,c,d,f,g),CalEllipse.b_prime(a,b,c,d,f,g)));
    }

    public static double rMin(float a, float b, float c,float d, float f, float g){
        return Math.round(Math.min(CalEllipse.a_prime(a, b, c, d, f, g), CalEllipse.b_prime(a, b, c, d, f, g)));
    }

    public static double phi(float a, float b, float c){
        return 0.5 * Math.acos((c - a) / (2 * b)); // ที่จริงมัน acot no acos
    }
}
