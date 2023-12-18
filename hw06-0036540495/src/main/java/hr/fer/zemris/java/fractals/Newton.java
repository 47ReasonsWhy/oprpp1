package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This program is a Newton-Raphson iteration-based fractal viewer.
 * It takes at least two roots from the standard input (terminated by the keyword `done`)
 * and then displays the fractal image of the polynomial
 * f(z) = (z - z_1)*(z - z_2)*...*(z - z_n)
 * painted using Newton-Raphson iteration.
 *
 * @see hr.fer.zemris.math.Complex
 * @see hr.fer.zemris.math.ComplexPolynomial
 * @see hr.fer.zemris.math.ComplexRootedPolynomial
 * @see hr.fer.zemris.java.fractals.viewer.FractalViewer
 * @see hr.fer.zemris.java.fractals.viewer.IFractalProducer
 * @see hr.fer.zemris.java.fractals.viewer.IFractalResultObserver
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class Newton {
    private static final double ROOT_THRESHOLD = 0.002;
    private static final double CONVERGENCE_THRESHOLD = 0.001;
    private static final int MAX_ITER = 16 * 16 * 16;
    private static ComplexRootedPolynomial rootedPolynomial;
    private static ComplexPolynomial polynomial;

    public static void main(String[] args) {
        System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
        System.out.println("Please enter at least two roots, one root per line. Enter 'done' when done.");

        short i = 1;
        List<Complex> roots = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.printf("Root %d> ", i);
            String line = sc.nextLine();
            if (line.equals("done")) break;

            Complex c;
            try {
                c = Complex.parseComplex(line);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid complex number format.");
            }
            roots.add(c);
            i++;
        }

        rootedPolynomial = new ComplexRootedPolynomial(Complex.ONE, roots.toArray(new Complex[0]));
        polynomial = rootedPolynomial.toComplexPolynomial();

        System.out.println("Image of fractal will appear shortly. Thank you.");
        FractalViewer.show(new NewtonProducer());
    }


    private static class NewtonProducer implements IFractalProducer {
        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height,
                            long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
            System.out.println("Zapocinjem izracun...");
            int offset = 0;
            short[] data = new short[width * height];
            for (int y = 0; y < height; y++) {
                if (cancel.get()) break;
                for (int x = 0; x < width; x++) {
                    double cre = (double) x / ((double) width - 1.0) * (reMax - reMin) + reMin;
                    double cim = (height - 1.0 - y) / ((double) height - 1) * (imMax - imMin) + imMin;
                    Complex zn = new Complex(cre, cim);
                    Complex znOld;
                    int iter = 0;
                    do {
                        znOld = new Complex(zn.getRe(), zn.getIm());
                        zn = zn.sub(polynomial.apply(zn).divide(polynomial.derive().apply(zn)));
                        iter++;
                    } while (zn.sub(znOld).module() > CONVERGENCE_THRESHOLD && iter < MAX_ITER);
                    int index = rootedPolynomial.indexOfClosestRootFor(zn, ROOT_THRESHOLD);
                    data[offset++] = (short) (index + 1);
                }
            }

            System.out.println("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
            observer.acceptResult(data, (short) (polynomial.order() + 1), requestNo);
        }
    }
}
