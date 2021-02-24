/*
 * Java source code for the thermal modeling of particle absorber with coupling medium and substrate
 * The resulting simulation reproduces the thermodynamic results of D=500 nm PMMA beads on silicon in air
 * Time-resolved thermodynamic can be simulated with Study 1, Step 2 with changing the heat source with volume_heat*rect_test
 * Instruction for compiling and running the code is available at:
 * https://www.comsol.com/blogs/automate-modeling-tasks-comsol-api-use-java/
 */

import com.comsol.model.*;
import com.comsol.model.util.*;

/** Model exported on Feb 22 2021, 19:31 by COMSOL 5.5.0.359. */
public class twostepstudy {

  public static Model run() {
    Model model = ModelUtil.create("Model");

    model.modelPath("D:\\Research\\Simulation for review");

    model.component().create("comp1", true);

    model.component("comp1").geom().create("geom1", 3);

    model.component("comp1").mesh().create("mesh1");

    model.component("comp1").physics().create("ht", "HeatTransfer", "geom1");

    model.study().create("std1");
    model.study("std1").create("stat", "Stationary");
    model.study("std1").feature("stat").activate("ht", true);
    model.study("std1").create("time", "Transient");

    model.component("comp1").geom("geom1").run();

    model.param().set("d_PMMA", "5000[nm]");
    model.param().descr("d_PMMA", "");
    model.param().set("r_PMMA", "d_PMMA/2");
    model.param().descr("r_PMMA", "");
    model.param().set("scale_factor", "0.5");
    model.param().descr("scale_factor", "");
    model.param().set("w", "r_PMMA*20*scale_factor");
    model.param().descr("w", "Width of physical geometry");
    model.param().set("t_pml", "r_PMMA*3*scale_factor");
    model.param().descr("t_pml", "PML thickness");
    model.param().set("t_air", "r_PMMA*12*scale_factor");
    model.param().descr("t_air", "Air domain height");
    model.param().set("t_si", "r_PMMA*1*scale_factor");
    model.param().descr("t_si", "Substrate domain height");
    model.param().set("d_PMMA", "500[nm]");
    model.param().set("r_contact", "0.4*r_PMMA");
    model.param().descr("r_contact", "");
    model.param().set("h_PMMA", "sqrt(r_PMMA^2-r_contact^2)");
    model.param().descr("h_PMMA", "");

    model.component("comp1").geom("geom1").create("sph1", "Sphere");
    model.component("comp1").geom("geom1").feature("sph1").set("r", "r_PMMA");
    model.component("comp1").geom("geom1").feature("sph1").set("pos", new String[]{"0", "0", "h_PMMA"});
    model.component("comp1").geom("geom1").run();
    model.component("comp1").geom("geom1").run("sph1");
    model.component("comp1").geom("geom1").create("blk1", "Block");
    model.component("comp1").geom("geom1").feature("blk1").set("size", new String[]{"w", "w", "t_air"});
    model.component("comp1").geom("geom1").feature("blk1").set("pos", new String[]{"0", "0", "0.5*t_air"});
    model.component("comp1").geom("geom1").run("blk1");
    model.component("comp1").geom("geom1").feature("blk1").set("base", "center");
    model.component("comp1").geom("geom1").run("blk1");
    model.component("comp1").geom("geom1").run("fin");
    model.component("comp1").geom("geom1").run("fin");
    model.component("comp1").geom("geom1").create("intsel1", "IntersectionSelection");
    model.component("comp1").geom("geom1").feature("intsel1").set("entitydim", -1);
    model.component("comp1").geom("geom1").feature().remove("intsel1");
    model.component("comp1").geom("geom1").run("blk1");
    model.component("comp1").geom("geom1").create("int1", "Intersection");
    model.component("comp1").geom("geom1").feature("int1").selection("input").set("blk1", "sph1");
    model.component("comp1").geom("geom1").run("int1");
    model.component("comp1").geom("geom1").run("fin");
    model.component("comp1").geom("geom1").run("int1");
    model.component("comp1").geom("geom1").create("blk2", "Block");
    model.component("comp1").geom("geom1").feature("blk2").set("size", new String[]{"w", "w", "t_air"});
    model.component("comp1").geom("geom1").feature("blk2").set("pos", new String[]{"0", "0", "0.5*t_air"});
    model.component("comp1").geom("geom1").run("blk2");
    model.component("comp1").geom("geom1").feature("blk2").set("base", "center");
    model.component("comp1").geom("geom1").run("blk2");
    model.component("comp1").geom("geom1").run("blk2");
    model.component("comp1").geom("geom1").create("blk3", "Block");
    model.component("comp1").geom("geom1").feature("blk3").set("size", new String[]{"w", "w", "t_si"});
    model.component("comp1").geom("geom1").run("blk3");
    model.component("comp1").geom("geom1").feature("blk3").set("pos", new String[]{"0", "0", "-0.5*t_si"});
    model.component("comp1").geom("geom1").feature("blk3").set("base", "center");
    model.component("comp1").geom("geom1").run("blk3");

    model.param().set("t_si", "r_PMMA*12*scale_factor");

    model.component("comp1").geom("geom1").run("blk3");
    model.component("comp1").geom("geom1").run("fin");

    model.param().rename("t_si", "t_substrate");

    model.component("comp1").geom("geom1").feature("blk3").set("size", new String[]{"w", "w", "t_substrate"});
    model.component("comp1").geom("geom1").feature("blk3").set("pos", new String[]{"0", "0", "-0.5*t_substrate"});
    model.component("comp1").geom("geom1").run("fin");

    model.param().rename("t_air", "t_medium");

    model.component("comp1").geom("geom1").feature("blk1").set("size", new String[]{"w", "w", "t_substrate"});
    model.component("comp1").geom("geom1").feature("blk1").set("pos", new String[]{"0", "0", "0.5*t_substrate"});
    model.component("comp1").geom("geom1").feature("blk2").set("pos", new String[]{"0", "0", "0.5*t_substrate"});
    model.component("comp1").geom("geom1").feature("blk2").set("size", new String[]{"w", "w", "t_substrate"});
    model.component("comp1").geom("geom1").run("blk2");
    model.component("comp1").geom("geom1").run("blk2");
    model.component("comp1").geom("geom1").run("fin");

    model.param().set("t_substrate", "r_PMMA*6*scale_factor");

    model.component("comp1").geom("geom1").run("fin");
    model.component("comp1").geom("geom1").feature("blk2").set("size", new String[]{"w", "w", "t_medium"});
    model.component("comp1").geom("geom1").feature("blk2").set("pos", new String[]{"0", "0", "0.5*t_medium"});
    model.component("comp1").geom("geom1").feature("blk1").set("size", new String[]{"w", "w", "t_medium"});
    model.component("comp1").geom("geom1").feature("blk1").set("pos", new String[]{"0", "0", "0.5*t_medium"});
    model.component("comp1").geom("geom1").run("blk1");
    model.component("comp1").geom("geom1").run("int1");
    model.component("comp1").geom("geom1").run("blk2");
    model.component("comp1").geom("geom1").run("blk3");
    model.component("comp1").geom("geom1").run("fin");

    model.component("comp1").material().create("mat1", "Common");
    model.component("comp1").material("mat1").label("Air [gas]");
    model.component("comp1").material("mat1").info().create("Composition");
    model.component("comp1").material("mat1").info("Composition")
         .body("78.09 N2, 20.95 O2, 0.93 Ar, 0.039 CO2, trace others (vol %)");
    model.component("comp1").material("mat1").propertyGroup("def").set("thermalconductivity", "k(T[1/K])[W/(m*K)]");
    model.component("comp1").material("mat1").propertyGroup("def").set("heatcapacity", "C(T[1/K])[J/(kg*K)]");
    model.component("comp1").material("mat1").propertyGroup("def").set("density", "rho_gas_2(T[1/K])[kg/m^3]");
    model.component("comp1").material("mat1").propertyGroup("def").set("TD", "TD(T[1/K])[m^2/s]");
    model.component("comp1").material("mat1").propertyGroup("def").set("dynamicviscosity", "eta(T[1/K])[Pa*s]");
    model.component("comp1").material("mat1").propertyGroup("def").func().create("k", "Piecewise");
    model.component("comp1").material("mat1").propertyGroup("def").func("k").set("funcname", "k");
    model.component("comp1").material("mat1").propertyGroup("def").func("k").set("arg", "T");
    model.component("comp1").material("mat1").propertyGroup("def").func("k").set("extrap", "constant");
    model.component("comp1").material("mat1").propertyGroup("def").func("k")
         .set("pieces", new String[][]{{"70.0", "1000.0", "-8.404165E-4+1.107418E-4*T^1-8.635537E-8*T^2+6.31411E-11*T^3-1.88168E-14*T^4"}});
    model.component("comp1").material("mat1").propertyGroup("def").func("k").label("Piecewise");
    model.component("comp1").material("mat1").propertyGroup("def").func("k").set("fununit", "");
    model.component("comp1").material("mat1").propertyGroup("def").func("k").set("argunit", "");
    model.component("comp1").material("mat1").propertyGroup("def").func().create("C", "Piecewise");
    model.component("comp1").material("mat1").propertyGroup("def").func("C").set("funcname", "C");
    model.component("comp1").material("mat1").propertyGroup("def").func("C").set("arg", "T");
    model.component("comp1").material("mat1").propertyGroup("def").func("C").set("extrap", "constant");
    model.component("comp1").material("mat1").propertyGroup("def").func("C")
         .set("pieces", new String[][]{{"100.0", "375.0", "1010.96988+0.0439478992*T^1-2.92239848E-4*T^2+6.50346734E-7*T^3"}, {"375.0", "1300.0", "1093.29008-0.63555211*T^1+0.00163399216*T^2-1.41293513E-6*T^3+5.59492011E-10*T^4-8.66307242E-14*T^5"}, {"1300.0", "3000.0", "701.080663+0.849386727*T^1-5.8464873E-4*T^2+2.30243637E-7*T^3-4.84675815E-11*T^4+4.2350197E-15*T^5"}});
    model.component("comp1").material("mat1").propertyGroup("def").func("C").label("Piecewise 1");
    model.component("comp1").material("mat1").propertyGroup("def").func("C").set("fununit", "");
    model.component("comp1").material("mat1").propertyGroup("def").func("C").set("argunit", "");
    model.component("comp1").material("mat1").propertyGroup("def").func().create("rho_gas_2", "Piecewise");
    model.component("comp1").material("mat1").propertyGroup("def").func("rho_gas_2").set("funcname", "rho_gas_2");
    model.component("comp1").material("mat1").propertyGroup("def").func("rho_gas_2").set("arg", "T");
    model.component("comp1").material("mat1").propertyGroup("def").func("rho_gas_2").set("extrap", "constant");
    model.component("comp1").material("mat1").propertyGroup("def").func("rho_gas_2")
         .set("pieces", new String[][]{{"80.0", "3000.0", "352.716*T^-1"}});
    model.component("comp1").material("mat1").propertyGroup("def").func("rho_gas_2").label("Piecewise 2");
    model.component("comp1").material("mat1").propertyGroup("def").func("rho_gas_2").set("fununit", "");
    model.component("comp1").material("mat1").propertyGroup("def").func("rho_gas_2").set("argunit", "");
    model.component("comp1").material("mat1").propertyGroup("def").func().create("TD", "Piecewise");
    model.component("comp1").material("mat1").propertyGroup("def").func("TD").set("funcname", "TD");
    model.component("comp1").material("mat1").propertyGroup("def").func("TD").set("arg", "T");
    model.component("comp1").material("mat1").propertyGroup("def").func("TD").set("extrap", "constant");
    model.component("comp1").material("mat1").propertyGroup("def").func("TD")
         .set("pieces", new String[][]{{"300.0", "753.0", "1.713214E-4-1.204913E-6*T^1+2.839046E-9*T^2-1.532799E-12*T^3"}, {"753.0", "873.0", "0.00416418-1.191227E-5*T^1+8.863636E-9*T^2"}});
    model.component("comp1").material("mat1").propertyGroup("def").func("TD").label("Piecewise 3");
    model.component("comp1").material("mat1").propertyGroup("def").func("TD").set("fununit", "");
    model.component("comp1").material("mat1").propertyGroup("def").func("TD").set("argunit", "");
    model.component("comp1").material("mat1").propertyGroup("def").func().create("eta", "Piecewise");
    model.component("comp1").material("mat1").propertyGroup("def").func("eta").set("funcname", "eta");
    model.component("comp1").material("mat1").propertyGroup("def").func("eta").set("arg", "T");
    model.component("comp1").material("mat1").propertyGroup("def").func("eta").set("extrap", "constant");
    model.component("comp1").material("mat1").propertyGroup("def").func("eta")
         .set("pieces", new String[][]{{"120.0", "600.0", "-1.132275E-7+7.94333E-8*T^1-7.197989E-11*T^2+5.158693E-14*T^3-1.592472E-17*T^4"}, {"600.0", "2150.0", "3.892629E-6+5.75387E-8*T^1-2.675811E-11*T^2+9.709691E-15*T^3-1.355541E-18*T^4"}});
    model.component("comp1").material("mat1").propertyGroup("def").func("eta").label("Piecewise 4");
    model.component("comp1").material("mat1").propertyGroup("def").func("eta").set("fununit", "");
    model.component("comp1").material("mat1").propertyGroup("def").func("eta").set("argunit", "");
    model.component("comp1").material("mat1").propertyGroup("def").addInput("temperature");
    model.component("comp1").material("mat1").set("family", "custom");
    model.component("comp1").material("mat1").set("lighting", "simple");
    model.component("comp1").material("mat1").set("ambient", "custom");
    model.component("comp1").material("mat1")
         .set("customambient", new double[]{0.9019607843137255, 0.9019607843137255, 1});
    model.component("comp1").material("mat1").set("diffuse", "custom");
    model.component("comp1").material("mat1")
         .set("customdiffuse", new double[]{0.9019607843137255, 0.9019607843137255, 1});
    model.component("comp1").material("mat1").set("noisescale", 0.08);
    model.component("comp1").material("mat1").set("noise", "on");
    model.component("comp1").material("mat1").set("noisefreq", 3);
    model.component("comp1").material("mat1").set("noise", "on");
    model.component("comp1").material("mat1").set("alpha", 1);
    model.component("comp1").material("mat1").set("shininess", 0);
    model.component("comp1").material().create("mat2", "Common");
    model.component("comp1").material("mat2").label("PMMA [solid]");
    model.component("comp1").material("mat2").info().create("Composition");
    model.component("comp1").material("mat2").info("Composition").body("PMMA (poly(methyl methacrylate))");
    model.component("comp1").material("mat2").propertyGroup("def").set("thermalconductivity", "k(T[1/K])[W/(m*K)]");
    model.component("comp1").material("mat2").propertyGroup("def")
         .set("thermalexpansioncoefficient", "(alpha(T[1/K])[1/K]+(Tempref-293[K])*if(abs(T-Tempref)>1e-3,(alpha(T[1/K])[1/K]-alpha(Tempref[1/K])[1/K])/(T-Tempref),d(alpha(T[1/K])[1/K],T)))/(1+alpha(Tempref[1/K])[1/K]*(Tempref-293[K]))");
    model.component("comp1").material("mat2").propertyGroup("def").set("density", "rho(T[1/K])[kg/m^3]");
    model.component("comp1").material("mat2").propertyGroup("def").set("TD", "TD(T[1/K])[m^2/s]");
    model.component("comp1").material("mat2").propertyGroup("def").func().create("k", "Piecewise");
    model.component("comp1").material("mat2").propertyGroup("def").func("k").set("funcname", "k");
    model.component("comp1").material("mat2").propertyGroup("def").func("k").set("arg", "T");
    model.component("comp1").material("mat2").propertyGroup("def").func("k").set("extrap", "constant");
    model.component("comp1").material("mat2").propertyGroup("def").func("k")
         .set("pieces", new String[][]{{"293.0", "352.0", "0.1510983+1.344534E-4*T^1"}});
    model.component("comp1").material("mat2").propertyGroup("def").func("k").label("Piecewise");
    model.component("comp1").material("mat2").propertyGroup("def").func("k").set("fununit", "");
    model.component("comp1").material("mat2").propertyGroup("def").func("k").set("argunit", "");
    model.component("comp1").material("mat2").propertyGroup("def").func().create("alpha", "Piecewise");
    model.component("comp1").material("mat2").propertyGroup("def").func("alpha").set("funcname", "alpha");
    model.component("comp1").material("mat2").propertyGroup("def").func("alpha").set("arg", "T");
    model.component("comp1").material("mat2").propertyGroup("def").func("alpha").set("extrap", "constant");
    model.component("comp1").material("mat2").propertyGroup("def").func("alpha")
         .set("pieces", new String[][]{{"293.0", "323.0", "5.840372E-5-4.831146E-7*T^1+1.757514E-9*T^2"}});
    model.component("comp1").material("mat2").propertyGroup("def").func("alpha").label("Piecewise 1");
    model.component("comp1").material("mat2").propertyGroup("def").func("alpha").set("fununit", "");
    model.component("comp1").material("mat2").propertyGroup("def").func("alpha").set("argunit", "");
    model.component("comp1").material("mat2").propertyGroup("def").func().create("rho", "Piecewise");
    model.component("comp1").material("mat2").propertyGroup("def").func("rho").set("funcname", "rho");
    model.component("comp1").material("mat2").propertyGroup("def").func("rho").set("arg", "T");
    model.component("comp1").material("mat2").propertyGroup("def").func("rho").set("extrap", "constant");
    model.component("comp1").material("mat2").propertyGroup("def").func("rho")
         .set("pieces", new String[][]{{"293.0", "323.0", "1082.529+0.974361*T^1-0.002073609*T^2"}});
    model.component("comp1").material("mat2").propertyGroup("def").func("rho").label("Piecewise 2");
    model.component("comp1").material("mat2").propertyGroup("def").func("rho").set("fununit", "");
    model.component("comp1").material("mat2").propertyGroup("def").func("rho").set("argunit", "");
    model.component("comp1").material("mat2").propertyGroup("def").func().create("TD", "Piecewise");
    model.component("comp1").material("mat2").propertyGroup("def").func("TD").set("funcname", "TD");
    model.component("comp1").material("mat2").propertyGroup("def").func("TD").set("arg", "T");
    model.component("comp1").material("mat2").propertyGroup("def").func("TD").set("extrap", "constant");
    model.component("comp1").material("mat2").propertyGroup("def").func("TD")
         .set("pieces", new String[][]{{"87.0", "308.0", "2.552466E-7-5.175922E-10*T^1+2.95724E-13*T^2"}, {"308.0", "460.0", "-7.896105E-6+8.38529E-8*T^1-3.249648E-10*T^2+5.518894E-13*T^3-3.476433E-16*T^4"}});
    model.component("comp1").material("mat2").propertyGroup("def").func("TD").label("Piecewise 3");
    model.component("comp1").material("mat2").propertyGroup("def").func("TD").set("fununit", "");
    model.component("comp1").material("mat2").propertyGroup("def").func("TD").set("argunit", "");
    model.component("comp1").material("mat2").propertyGroup("def").addInput("temperature");
    model.component("comp1").material("mat2").propertyGroup("def").addInput("strainreferencetemperature");
    model.component("comp1").material("mat2").propertyGroup().create("ThermalExpansion", "Thermal expansion");
    model.component("comp1").material("mat2").propertyGroup("ThermalExpansion")
         .set("dL", "(dL(T[1/K])-dL(Tempref[1/K]))/(1+dL(Tempref[1/K]))");
    model.component("comp1").material("mat2").propertyGroup("ThermalExpansion")
         .set("dLIso", "(dL(T[1/K])-dL(Tempref[1/K]))/(1+dL(Tempref[1/K]))");
    model.component("comp1").material("mat2").propertyGroup("ThermalExpansion").func().create("dL", "Piecewise");
    model.component("comp1").material("mat2").propertyGroup("ThermalExpansion").func("dL").set("funcname", "dL");
    model.component("comp1").material("mat2").propertyGroup("ThermalExpansion").func("dL").set("arg", "T");
    model.component("comp1").material("mat2").propertyGroup("ThermalExpansion").func("dL").set("extrap", "constant");
    model.component("comp1").material("mat2").propertyGroup("ThermalExpansion").func("dL")
         .set("pieces", new String[][]{{"293.0", "323.0", "0.031294866-2.81029E-4*T^1+5.946095E-7*T^2"}});
    model.component("comp1").material("mat2").propertyGroup("ThermalExpansion").func("dL").label("Piecewise");
    model.component("comp1").material("mat2").propertyGroup("ThermalExpansion").func("dL").set("fununit", "");
    model.component("comp1").material("mat2").propertyGroup("ThermalExpansion").func("dL").set("argunit", "");
    model.component("comp1").material("mat2").propertyGroup("ThermalExpansion").addInput("temperature");
    model.component("comp1").material("mat2").propertyGroup("ThermalExpansion")
         .addInput("strainreferencetemperature");
    model.component("comp1").material("mat2").propertyGroup().create("Enu", "Young's modulus and Poisson's ratio");
    model.component("comp1").material("mat2").propertyGroup("Enu").set("youngsmodulus", "E(T[1/K])[Pa]");
    model.component("comp1").material("mat2").propertyGroup("Enu").set("poissonsratio", "nu(T[1/K])");
    model.component("comp1").material("mat2").propertyGroup("Enu").func().create("E", "Piecewise");
    model.component("comp1").material("mat2").propertyGroup("Enu").func("E").set("funcname", "E");
    model.component("comp1").material("mat2").propertyGroup("Enu").func("E").set("arg", "T");
    model.component("comp1").material("mat2").propertyGroup("Enu").func("E").set("extrap", "constant");
    model.component("comp1").material("mat2").propertyGroup("Enu").func("E")
         .set("pieces", new String[][]{{"68.0", "125.0", "7.3846E9"}, {"125.0", "400.0", "4.31023E9+6.934433E7*T^1-528212.9*T^2+1579.615*T^3-1.742098*T^4"}});
    model.component("comp1").material("mat2").propertyGroup("Enu").func("E").label("Piecewise");
    model.component("comp1").material("mat2").propertyGroup("Enu").func("E").set("fununit", "");
    model.component("comp1").material("mat2").propertyGroup("Enu").func("E").set("argunit", "");
    model.component("comp1").material("mat2").propertyGroup("Enu").func().create("nu", "Piecewise");
    model.component("comp1").material("mat2").propertyGroup("Enu").func("nu").set("funcname", "nu");
    model.component("comp1").material("mat2").propertyGroup("Enu").func("nu").set("arg", "T");
    model.component("comp1").material("mat2").propertyGroup("Enu").func("nu").set("extrap", "constant");
    model.component("comp1").material("mat2").propertyGroup("Enu").func("nu")
         .set("pieces", new String[][]{{"68.0", "124.0", "0.34502"}, {"124.0", "270.0", "0.6787961-0.006076254*T^1+3.893231E-5*T^2-1.066899E-7*T^3+1.037697E-10*T^4"}, {"270.0", "400.0", "6.00413-0.07428949*T^1+3.664807E-4*T^2-8.064015E-7*T^3+6.657131E-10*T^4"}});
    model.component("comp1").material("mat2").propertyGroup("Enu").func("nu").label("Piecewise 1");
    model.component("comp1").material("mat2").propertyGroup("Enu").func("nu").set("fununit", "");
    model.component("comp1").material("mat2").propertyGroup("Enu").func("nu").set("argunit", "");
    model.component("comp1").material("mat2").propertyGroup("Enu").addInput("temperature");
    model.component("comp1").material("mat2").propertyGroup().create("KG", "Bulk modulus and shear modulus");
    model.component("comp1").material("mat2").propertyGroup("KG").set("G", "mu(T[1/K])[Pa]");
    model.component("comp1").material("mat2").propertyGroup("KG").set("K", "kappa(T[1/K])[Pa]");
    model.component("comp1").material("mat2").propertyGroup("KG").func().create("mu", "Piecewise");
    model.component("comp1").material("mat2").propertyGroup("KG").func("mu").set("funcname", "mu");
    model.component("comp1").material("mat2").propertyGroup("KG").func("mu").set("arg", "T");
    model.component("comp1").material("mat2").propertyGroup("KG").func("mu").set("extrap", "constant");
    model.component("comp1").material("mat2").propertyGroup("KG").func("mu")
         .set("pieces", new String[][]{{"68.0", "125.0", "2.7459E9"}, {"125.0", "400.0", "8.498712E8+3.971458E7*T^1-287911.7*T^2+846.344*T^3-0.9122308*T^4"}});
    model.component("comp1").material("mat2").propertyGroup("KG").func("mu").label("Piecewise");
    model.component("comp1").material("mat2").propertyGroup("KG").func("mu").set("fununit", "");
    model.component("comp1").material("mat2").propertyGroup("KG").func("mu").set("argunit", "");
    model.component("comp1").material("mat2").propertyGroup("KG").func().create("kappa", "Piecewise");
    model.component("comp1").material("mat2").propertyGroup("KG").func("kappa").set("funcname", "kappa");
    model.component("comp1").material("mat2").propertyGroup("KG").func("kappa").set("arg", "T");
    model.component("comp1").material("mat2").propertyGroup("KG").func("kappa").set("extrap", "constant");
    model.component("comp1").material("mat2").propertyGroup("KG").func("kappa")
         .set("pieces", new String[][]{{"68.0", "124.0", "7.923015E9"}, {"124.0", "400.0", "2.054283E10-2.194785E8*T^1+1354634.0*T^2-3721.425*T^3+3.647352*T^4"}});
    model.component("comp1").material("mat2").propertyGroup("KG").func("kappa").label("Piecewise 1");
    model.component("comp1").material("mat2").propertyGroup("KG").func("kappa").set("fununit", "");
    model.component("comp1").material("mat2").propertyGroup("KG").func("kappa").set("argunit", "");
    model.component("comp1").material("mat2").propertyGroup("KG").addInput("temperature");
    model.component("comp1").material().create("mat3", "Common");
    model.component("comp1").material("mat3").label("Silicon [solid,bulk]");
    model.component("comp1").material("mat3").propertyGroup("def")
         .set("thermalconductivity", "k_solid_bulk_1(T[1/K])[W/(m*K)]");
    model.component("comp1").material("mat3").propertyGroup("def")
         .set("thermalexpansioncoefficient", "(alpha_solid_1(T[1/K])[1/K]+(Tempref-293[K])*if(abs(T-Tempref)>1e-3,(alpha_solid_1(T[1/K])[1/K]-alpha_solid_1(Tempref[1/K])[1/K])/(T-Tempref),d(alpha_solid_1(T[1/K])[1/K],T)))/(1+alpha_solid_1(Tempref[1/K])[1/K]*(Tempref-293[K]))");
    model.component("comp1").material("mat3").propertyGroup("def")
         .set("heatcapacity", "C_solid_1(T[1/K])[J/(kg*K)]");
    model.component("comp1").material("mat3").propertyGroup("def").set("HC", "HC_solid_1(T[1/K])[J/(mol*K)]");
    model.component("comp1").material("mat3").propertyGroup("def").set("VP", "VP_solid_1(T[1/K])[Pa]");
    model.component("comp1").material("mat3").propertyGroup("def").set("density", "rho_solid_1(T[1/K])[kg/m^3]");
    model.component("comp1").material("mat3").propertyGroup("def").set("TD", "TD(T[1/K])[m^2/s]");
    model.component("comp1").material("mat3").propertyGroup("def").func().create("k_solid_bulk_1", "Piecewise");
    model.component("comp1").material("mat3").propertyGroup("def").func("k_solid_bulk_1")
         .set("funcname", "k_solid_bulk_1");
    model.component("comp1").material("mat3").propertyGroup("def").func("k_solid_bulk_1").set("arg", "T");
    model.component("comp1").material("mat3").propertyGroup("def").func("k_solid_bulk_1").set("extrap", "constant");
    model.component("comp1").material("mat3").propertyGroup("def").func("k_solid_bulk_1")
         .set("pieces", new String[][]{{"0.0", "8.0", "1.0E-15-0.2498417*T^1+1.481083*T^2+5.733086*T^3-0.3609236*T^4"}, 
         {"8.0", "35.0", "2955.562-1163.24*T^1+195.2953*T^2-10.93608*T^3+0.2648092*T^4-0.002396056*T^5"}, 
         {"35.0", "100.0", "14141.66-506.8115*T^1+8.440371*T^2-0.06860715*T^3+2.162919E-4*T^4"}, 
         {"100.0", "273.0", "5061.885-79.31105*T^1+0.5061928*T^2-0.001469493*T^3+1.607847E-6*T^4"}, 
         {"273.0", "1000.0", "810.9131-4.713958*T^1+0.01248387*T^2-1.698128E-5*T^3+1.152568E-8*T^4-3.094028E-12*T^5"}, 
         {"1000.0", "1685.0", "393.8803-0.980598*T^1+9.947652E-4*T^2-4.55816E-7*T^3+7.898531E-11*T^4"}});
    model.component("comp1").material("mat3").propertyGroup("def").func("k_solid_bulk_1").label("Piecewise");
    model.component("comp1").material("mat3").propertyGroup("def").func("k_solid_bulk_1").set("fununit", "");
    model.component("comp1").material("mat3").propertyGroup("def").func("k_solid_bulk_1").set("argunit", "");
    model.component("comp1").material("mat3").propertyGroup("def").func().create("alpha_solid_1", "Piecewise");
    model.component("comp1").material("mat3").propertyGroup("def").func("alpha_solid_1")
         .set("funcname", "alpha_solid_1");
    model.component("comp1").material("mat3").propertyGroup("def").func("alpha_solid_1").set("arg", "T");
    model.component("comp1").material("mat3").propertyGroup("def").func("alpha_solid_1").set("extrap", "constant");
    model.component("comp1").material("mat3").propertyGroup("def").func("alpha_solid_1")
         .set("pieces", new String[][]{{"0.0", "30.0", "7.35637E-7+2.453566E-9*T^1+1.20482E-11*T^2"}, 
         {"30.0", "130.0", "7.713685E-7+2.098318E-10*T^1+4.628581E-11*T^2+7.569451E-14*T^3-8.713366E-16*T^4"}, 
         {"130.0", "293.0", "-3.223163E-7+2.257142E-8*T^1-9.684044E-11*T^2+2.835316E-13*T^3-3.440569E-16*T^4"}, 
         {"293.0", "1000.0", "6.772622E-7+9.501405E-9*T^1-1.271286E-11*T^2+8.584038E-15*T^3-2.241706E-18*T^4"}});
    model.component("comp1").material("mat3").propertyGroup("def").func("alpha_solid_1").label("Piecewise 1");
    model.component("comp1").material("mat3").propertyGroup("def").func("alpha_solid_1").set("fununit", "");
    model.component("comp1").material("mat3").propertyGroup("def").func("alpha_solid_1").set("argunit", "");
    model.component("comp1").material("mat3").propertyGroup("def").func().create("C_solid_1", "Piecewise");
    model.component("comp1").material("mat3").propertyGroup("def").func("C_solid_1").set("funcname", "C_solid_1");
    model.component("comp1").material("mat3").propertyGroup("def").func("C_solid_1").set("arg", "T");
    model.component("comp1").material("mat3").propertyGroup("def").func("C_solid_1").set("extrap", "constant");
    model.component("comp1").material("mat3").propertyGroup("def").func("C_solid_1")
         .set("pieces", new String[][]{{"1.0", "7.0", "-4.8321811E-5+7.68448084E-5*T^1-3.41813386E-5*T^2+2.80830708E-4*T^3-3.12897302E-7*T^4"}, 
         {"7.0", "20.0", "0.0525075264-0.0396481488*T^1+0.0100460936*T^2-7.81251542E-4*T^3+3.9615568E-5*T^4"}, 
         {"20.0", "50.0", "-1.80567549+0.761903471*T^1-0.0865373791*T^2+0.0037353614*T^3-3.33397563E-5*T^4"}, 
         {"50.0", "293.0", "-82.9482602+2.71223532*T^1+0.0140475122*T^2-7.97769138E-5*T^3+1.07990546E-7*T^4"}, 
         {"293.0", "900.0", "63.0442191+3.7706731*T^1-0.00694853616*T^2+5.9532044E-6*T^3-1.91438418E-9*T^4"}, 
         {"900.0", "1685.0", "769.459775+0.187175131*T^1-3.18395957E-5*T^2"}});
    model.component("comp1").material("mat3").propertyGroup("def").func("C_solid_1").label("Piecewise 2");
    model.component("comp1").material("mat3").propertyGroup("def").func("C_solid_1").set("fununit", "");
    model.component("comp1").material("mat3").propertyGroup("def").func("C_solid_1").set("argunit", "");
    model.component("comp1").material("mat3").propertyGroup("def").func().create("HC_solid_1", "Piecewise");
    model.component("comp1").material("mat3").propertyGroup("def").func("HC_solid_1").set("funcname", "HC_solid_1");
    model.component("comp1").material("mat3").propertyGroup("def").func("HC_solid_1").set("arg", "T");
    model.component("comp1").material("mat3").propertyGroup("def").func("HC_solid_1").set("extrap", "constant");
    model.component("comp1").material("mat3").propertyGroup("def").func("HC_solid_1")
         .set("pieces", new String[][]{{"1.0", "7.0", "-1.35714274E-6+2.15822519E-6*T^1-9.59999972E-7*T^2+7.88727095E-6*T^3-8.78787695E-9*T^4"}, 
         {"7.0", "20.0", "0.00147469975-0.00111353813*T^1+2.82149588E-4*T^2-2.19418374E-5*T^3+1.11262309E-6*T^4"}, 
         {"20.0", "50.0", "-0.0507133017+0.0213984404*T^1-0.00243044543*T^2+1.0490949E-4*T^3-9.36363719E-7*T^4"}, 
         {"50.0", "293.0", "-2.32964367+0.0761744898*T^1+3.9453141E-4*T^2-2.2405751E-6*T^3+3.03296821E-9*T^4"}, 
         {"293.0", "900.0", "1.3923419+0.109040605*T^1-2.04395094E-4*T^2+1.78689895E-7*T^3-5.88857415E-11*T^4"}, 
         {"900.0", "1685.0", "22.0332118+0.0045851661*T^1-6.45226774E-7*T^2"}});
    model.component("comp1").material("mat3").propertyGroup("def").func("HC_solid_1").label("Piecewise 3");
    model.component("comp1").material("mat3").propertyGroup("def").func("HC_solid_1").set("fununit", "");
    model.component("comp1").material("mat3").propertyGroup("def").func("HC_solid_1").set("argunit", "");
    model.component("comp1").material("mat3").propertyGroup("def").func().create("VP_solid_1", "Piecewise");
    model.component("comp1").material("mat3").propertyGroup("def").func("VP_solid_1").set("funcname", "VP_solid_1");
    model.component("comp1").material("mat3").propertyGroup("def").func("VP_solid_1").set("arg", "T");
    model.component("comp1").material("mat3").propertyGroup("def").func("VP_solid_1").set("extrap", "constant");
    model.component("comp1").material("mat3").propertyGroup("def").func("VP_solid_1")
         .set("pieces", new String[][]{{"293.0", "1685.0", "(exp((-2.35500000e+04/T-5.65000000e-01*log10(T)+1.23500000e+01)*log(10.0)))*1.33320000e+02"}});
    model.component("comp1").material("mat3").propertyGroup("def").func("VP_solid_1").label("Piecewise 4");
    model.component("comp1").material("mat3").propertyGroup("def").func("VP_solid_1").set("fununit", "");

    return model;
  }

  public static Model run2(Model model) {
    model.component("comp1").material("mat3").propertyGroup("def").func("VP_solid_1").set("argunit", "");
    model.component("comp1").material("mat3").propertyGroup("def").func().create("rho_solid_1", "Piecewise");
    model.component("comp1").material("mat3").propertyGroup("def").func("rho_solid_1")
         .set("funcname", "rho_solid_1");
    model.component("comp1").material("mat3").propertyGroup("def").func("rho_solid_1").set("arg", "T");
    model.component("comp1").material("mat3").propertyGroup("def").func("rho_solid_1").set("extrap", "constant");
    model.component("comp1").material("mat3").propertyGroup("def").func("rho_solid_1")
         .set("pieces", new String[][]{{"0.0", "30.0", "2331.507-7.113612E-5*T^1+3.674386E-6*T^2"}, 
         {"30.0", "130.0", "2331.592-0.005873649*T^1+1.206114E-4*T^2-5.479876E-7*T^3+1.606517E-10*T^4"}, 
         {"130.0", "293.0", "2330.436+0.02130626*T^1-9.544145E-5*T^2+4.607415E-8*T^3+4.840886E-11*T^4"}, 
         {"293.0", "1000.0", "2332.565+0.003839515*T^1-5.433308E-5*T^2+4.287211E-8*T^3-1.366545E-11*T^4"}});
    model.component("comp1").material("mat3").propertyGroup("def").func("rho_solid_1").label("Piecewise 5");
    model.component("comp1").material("mat3").propertyGroup("def").func("rho_solid_1").set("fununit", "");
    model.component("comp1").material("mat3").propertyGroup("def").func("rho_solid_1").set("argunit", "");
    model.component("comp1").material("mat3").propertyGroup("def").func().create("TD", "Piecewise");
    model.component("comp1").material("mat3").propertyGroup("def").func("TD").set("funcname", "TD");
    model.component("comp1").material("mat3").propertyGroup("def").func("TD").set("arg", "T");
    model.component("comp1").material("mat3").propertyGroup("def").func("TD").set("extrap", "constant");
    model.component("comp1").material("mat3").propertyGroup("def").func("TD")
         .set("pieces", new String[][]{{"1.0", "12.0", "12.40366-2.652049*T^1+0.5813105*T^2-0.08018325*T^3+0.005288731*T^4-1.314169E-4*T^5"}, 
         {"12.0", "36.0", "12.40379-1.240777*T^1+0.03837706*T^2-9.696578E-6*T^3-1.861403E-5*T^4+2.36465E-7*T^5"}, 
         {"36.0", "70.0", "2.231412-0.1770592*T^1+0.005725565*T^2-9.331438E-5*T^3+7.623105E-7*T^4-2.489437E-9*T^5"}, 
         {"70.0", "165.0", "0.08907341-0.003083678*T^1+4.419818E-5*T^2-3.22047E-7*T^3+1.182074E-9*T^4-1.739399E-12*T^5"}, 
         {"165.0", "305.0", "0.01190219-2.287791E-4*T^1+1.824052E-6*T^2-7.358192E-9*T^3+1.487663E-11*T^4-1.200563E-14*T^5"}, 
         {"305.0", "735.0", "7.622018E-4-5.322058E-6*T^1+1.632442E-8*T^2-2.584688E-11*T^3+2.077321E-14*T^4-6.740584E-18*T^5"}, 
         {"735.0", "1000.0", "-2.043535E-4+1.357482E-6*T^1-2.753022E-9*T^2+2.336409E-12*T^3-7.219021E-16*T^4"}});
    model.component("comp1").material("mat3").propertyGroup("def").func("TD").label("Piecewise 6");
    model.component("comp1").material("mat3").propertyGroup("def").func("TD").set("fununit", "");
    model.component("comp1").material("mat3").propertyGroup("def").func("TD").set("argunit", "");
    model.component("comp1").material("mat3").propertyGroup("def").addInput("temperature");
    model.component("comp1").material("mat3").propertyGroup("def").addInput("strainreferencetemperature");
    model.component("comp1").material("mat3").propertyGroup().create("ThermalExpansion", "Thermal expansion");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion")
         .set("dL", "(dL_solid_1(T[1/K])-dL_solid_1(Tempref[1/K]))/(1+dL_solid_1(Tempref[1/K]))");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion")
         .set("dLIso", "(dL_solid_1(T[1/K])-dL_solid_1(Tempref[1/K]))/(1+dL_solid_1(Tempref[1/K]))");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").set("alphatan", "CTE(T[1/K])[1/K]");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion")
         .set("alphatanIso", "CTE(T[1/K])[1/K]");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").func()
         .create("dL_solid_1", "Piecewise");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").func("dL_solid_1")
         .set("funcname", "dL_solid_1");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").func("dL_solid_1").set("arg", "T");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").func("dL_solid_1")
         .set("extrap", "constant");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").func("dL_solid_1")
         .set("pieces", new String[][]{{"0.0", "30.0", "-2.154537E-4"}, 
         {"30.0", "130.0", "-2.276144E-4+8.396104E-7*T^1-1.724143E-8*T^2+7.834799E-11*T^3-2.303956E-14*T^4"}, 
         {"130.0", "293.0", "-5.217231E-5-3.263667E-6*T^1+1.532991E-8*T^2-1.223001E-11*T^3"}, 
         {"293.0", "1000.0", "-5.844527E-4+1.124129E-6*T^1+3.311476E-9*T^2-1.161022E-12*T^3"}});
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").func("dL_solid_1")
         .label("Piecewise");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").func("dL_solid_1")
         .set("fununit", "");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").func("dL_solid_1")
         .set("argunit", "");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").func().create("CTE", "Piecewise");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").func("CTE").set("funcname", "CTE");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").func("CTE").set("arg", "T");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").func("CTE")
         .set("extrap", "constant");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").func("CTE")
         .set("pieces", new String[][]{{"0.0", "35.0", "-1.128708E-10+4.013852E-10*T^1-1.52474E-10*T^2+1.935922E-11*T^3-8.512984E-13*T^4+9.848876E-15*T^5"}, 
         {"35.0", "121.0", "-4.536302E-7+5.088778E-8*T^1-1.847617E-9*T^2+2.348401E-11*T^3-1.242463E-13*T^4+2.442997E-16*T^5"}, 
         {"121.0", "293.0", "8.574679E-8-3.514147E-8*T^1+4.332654E-10*T^2-1.449023E-12*T^3+1.630929E-15*T^4"}, 
         {"293.0", "1000.0", "-2.621356E-6+3.020773E-8*T^1-5.538848E-11*T^2+4.755059E-14*T^3-1.534596E-17*T^4"}});
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").func("CTE").label("Piecewise 1");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").func("CTE").set("fununit", "");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").func("CTE").set("argunit", "");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion").addInput("temperature");
    model.component("comp1").material("mat3").propertyGroup("ThermalExpansion")
         .addInput("strainreferencetemperature");
    model.component("comp1").material("mat3").set("family", "custom");
    model.component("comp1").material("mat3").set("lighting", "cooktorrance");
    model.component("comp1").material("mat3").set("specular", "custom");
    model.component("comp1").material("mat3").set("customspecular", new double[]{0.7843137254901961, 1, 1});
    model.component("comp1").material("mat3").set("fresnel", 0.9);
    model.component("comp1").material("mat3").set("roughness", 0.1);
    model.component("comp1").material("mat3").set("shininess", 200);
    model.component("comp1").material().create("mat4", "Common");
    model.component("comp1").material("mat4").propertyGroup("def").func().create("eta", "Piecewise");
    model.component("comp1").material("mat4").propertyGroup("def").func().create("Cp", "Piecewise");
    model.component("comp1").material("mat4").propertyGroup("def").func().create("rho", "Piecewise");
    model.component("comp1").material("mat4").propertyGroup("def").func().create("k", "Piecewise");
    model.component("comp1").material("mat4").label("Glycerol");
    model.component("comp1").material("mat4").set("family", "water");
    model.component("comp1").material("mat4").set("groups", new String[][]{{"liquids", "Liquids"}});
    model.component("comp1").material("mat4").propertyGroup("def").func("eta").set("arg", "T");
    model.component("comp1").material("mat4").propertyGroup("def").func("eta")
         .set("pieces", new String[][]{{"273.15", "323.15", "318121.207-5155.22816*T^1+33.4268277*T^2-0.108396057*T^3+1.75782331E-4*T^4-1.14036942E-7*T^5"}});
    model.component("comp1").material("mat4").propertyGroup("def").func("eta").set("argunit", "K");
    model.component("comp1").material("mat4").propertyGroup("def").func("eta").set("fununit", "Pa*s");
    model.component("comp1").material("mat4").propertyGroup("def").func("Cp").set("arg", "T");
    model.component("comp1").material("mat4").propertyGroup("def").func("Cp")
         .set("pieces", new String[][]{{"273.15", "323.15", "502.69181+6.42285714*T^1"}});
    model.component("comp1").material("mat4").propertyGroup("def").func("Cp").set("argunit", "K");
    model.component("comp1").material("mat4").propertyGroup("def").func("Cp").set("fununit", "J/(kg*K)");
    model.component("comp1").material("mat4").propertyGroup("def").func("rho").set("arg", "T");
    model.component("comp1").material("mat4").propertyGroup("def").func("rho")
         .set("pieces", new String[][]{{"273.15", "323.15", "1444.95596-0.617371429*T^1"}});
    model.component("comp1").material("mat4").propertyGroup("def").func("rho").set("argunit", "K");
    model.component("comp1").material("mat4").propertyGroup("def").func("rho").set("fununit", "kg/m^3");
    model.component("comp1").material("mat4").propertyGroup("def").func("k").set("arg", "T");
    model.component("comp1").material("mat4").propertyGroup("def").func("k")
         .set("pieces", new String[][]{{"273.15", "323.15", "-2.63102624+0.0285832832*T^1-9.34228175E-5*T^2+1.01851852E-7*T^3"}});
    model.component("comp1").material("mat4").propertyGroup("def").func("k").set("argunit", "K");
    model.component("comp1").material("mat4").propertyGroup("def").func("k").set("fununit", "W/(m*K)");
    model.component("comp1").material("mat4").propertyGroup("def").set("dynamicviscosity", "eta(T)");
    model.component("comp1").material("mat4").propertyGroup("def").descr("dynamicviscosity_symmetry", "");
    model.component("comp1").material("mat4").propertyGroup("def").set("heatcapacity", "Cp(T)");
    model.component("comp1").material("mat4").propertyGroup("def").descr("heatcapacity_symmetry", "");
    model.component("comp1").material("mat4").propertyGroup("def").set("density", "rho(T)");
    model.component("comp1").material("mat4").propertyGroup("def").descr("density_symmetry", "");
    model.component("comp1").material("mat4").propertyGroup("def")
         .set("thermalconductivity", new String[]{"k(T)", "0", "0", "0", "k(T)", "0", "0", "0", "k(T)"});
    model.component("comp1").material("mat4").propertyGroup("def").descr("thermalconductivity_symmetry", "");
    model.component("comp1").material("mat4").propertyGroup("def").addInput("temperature");
    model.component("comp1").material("mat4").set("groups", new String[][]{});
    model.component("comp1").material("mat4").set("family", "water");
    model.component("comp1").material().create("mat5", "Common");
    model.component("comp1").material("mat5").label("H2O (water) [liquid]");
    model.component("comp1").material("mat5").info().create("Composition");
    model.component("comp1").material("mat5").info("Composition")
         .body("11.2 H, 88.8 O (wt%) or 66.7 H, 33.3 O (at%), MW = 18.02 g/mol");
    model.component("comp1").material("mat5").propertyGroup("def")
         .set("thermalconductivity", "k_liquid_2(T[1/K])[W/(m*K)]");
    model.component("comp1").material("mat5").propertyGroup("def")
         .set("thermalexpansioncoefficient", "(alpha_liquid_2(T[1/K])[1/K]+(Tempref-293[K])*if(abs(T-Tempref)>1e-3,(alpha_liquid_2(T[1/K])[1/K]-alpha_liquid_2(Tempref[1/K])[1/K])/(T-Tempref),d(alpha_liquid_2(T[1/K])[1/K],T)))/(1+alpha_liquid_2(Tempref[1/K])[1/K]*(Tempref-293[K]))");
    model.component("comp1").material("mat5").propertyGroup("def")
         .set("heatcapacity", "C_liquid_2(T[1/K])[J/(kg*K)]");
    model.component("comp1").material("mat5").propertyGroup("def").set("HC", "HC_liquid_2(T[1/K])[J/(mol*K)]");
    model.component("comp1").material("mat5").propertyGroup("def").set("VP", "VP_liquid_2(T[1/K])[Pa]");
    model.component("comp1").material("mat5").propertyGroup("def").set("density", "rho_liquid_2(T[1/K])[kg/m^3]");
    model.component("comp1").material("mat5").propertyGroup("def").set("TD", "TD_liquid_2(T[1/K])[m^2/s]");
    model.component("comp1").material("mat5").propertyGroup("def")
         .set("dynamicviscosity", "eta_liquid_1(T[1/K])[Pa*s]");
    model.component("comp1").material("mat5").propertyGroup("def").set("SurfF", "SurfF(T[1/K])[N/m]");
    model.component("comp1").material("mat5").propertyGroup("def").func().create("k_liquid_2", "Piecewise");
    model.component("comp1").material("mat5").propertyGroup("def").func("k_liquid_2").set("funcname", "k_liquid_2");
    model.component("comp1").material("mat5").propertyGroup("def").func("k_liquid_2").set("arg", "T");
    model.component("comp1").material("mat5").propertyGroup("def").func("k_liquid_2").set("extrap", "constant");
    model.component("comp1").material("mat5").propertyGroup("def").func("k_liquid_2")
         .set("pieces", new String[][]{{"275.0", "370.0", "-0.9003748+0.008387698*T^1-1.118205E-5*T^2"}});
    model.component("comp1").material("mat5").propertyGroup("def").func("k_liquid_2").label("Piecewise");
    model.component("comp1").material("mat5").propertyGroup("def").func("k_liquid_2").set("fununit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func("k_liquid_2").set("argunit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func().create("alpha_liquid_2", "Piecewise");
    model.component("comp1").material("mat5").propertyGroup("def").func("alpha_liquid_2")
         .set("funcname", "alpha_liquid_2");
    model.component("comp1").material("mat5").propertyGroup("def").func("alpha_liquid_2").set("arg", "T");
    model.component("comp1").material("mat5").propertyGroup("def").func("alpha_liquid_2").set("extrap", "constant");
    model.component("comp1").material("mat5").propertyGroup("def").func("alpha_liquid_2")
         .set("pieces", new String[][]{{"273.0", "283.0", "0.01032507-7.62815E-5*T^1+1.412474E-7*T^2"}, {"283.0", "373.0", "-0.002464185+1.947611E-5*T^1-5.049672E-8*T^2+4.616995E-11*T^3"}});
    model.component("comp1").material("mat5").propertyGroup("def").func("alpha_liquid_2").label("Piecewise 1");
    model.component("comp1").material("mat5").propertyGroup("def").func("alpha_liquid_2").set("fununit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func("alpha_liquid_2").set("argunit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func().create("C_liquid_2", "Piecewise");
    model.component("comp1").material("mat5").propertyGroup("def").func("C_liquid_2").set("funcname", "C_liquid_2");
    model.component("comp1").material("mat5").propertyGroup("def").func("C_liquid_2").set("arg", "T");
    model.component("comp1").material("mat5").propertyGroup("def").func("C_liquid_2").set("extrap", "constant");
    model.component("comp1").material("mat5").propertyGroup("def").func("C_liquid_2")
         .set("pieces", new String[][]{{"293.0", "373.0", "4035.84079+0.492312034*T^1"}});
    model.component("comp1").material("mat5").propertyGroup("def").func("C_liquid_2").label("Piecewise 2");
    model.component("comp1").material("mat5").propertyGroup("def").func("C_liquid_2").set("fununit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func("C_liquid_2").set("argunit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func().create("HC_liquid_2", "Piecewise");
    model.component("comp1").material("mat5").propertyGroup("def").func("HC_liquid_2")
         .set("funcname", "HC_liquid_2");
    model.component("comp1").material("mat5").propertyGroup("def").func("HC_liquid_2").set("arg", "T");
    model.component("comp1").material("mat5").propertyGroup("def").func("HC_liquid_2").set("extrap", "constant");
    model.component("comp1").material("mat5").propertyGroup("def").func("HC_liquid_2")
         .set("pieces", new String[][]{{"293.0", "373.0", "72.6451184+0.00886161577*T^1"}});
    model.component("comp1").material("mat5").propertyGroup("def").func("HC_liquid_2").label("Piecewise 3");
    model.component("comp1").material("mat5").propertyGroup("def").func("HC_liquid_2").set("fununit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func("HC_liquid_2").set("argunit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func().create("VP_liquid_2", "Piecewise");
    model.component("comp1").material("mat5").propertyGroup("def").func("VP_liquid_2")
         .set("funcname", "VP_liquid_2");
    model.component("comp1").material("mat5").propertyGroup("def").func("VP_liquid_2").set("arg", "T");
    model.component("comp1").material("mat5").propertyGroup("def").func("VP_liquid_2").set("extrap", "constant");
    model.component("comp1").material("mat5").propertyGroup("def").func("VP_liquid_2")
         .set("pieces", new String[][]{{"280.0", "600.0", "(exp((-2.00512183e+03/T-5.56570000e-01*log10(T)+9.89879000e+00-1.11169009e+07/T^3)*log(10.0)))*1.33320000e+02"}});
    model.component("comp1").material("mat5").propertyGroup("def").func("VP_liquid_2").label("Piecewise 4");
    model.component("comp1").material("mat5").propertyGroup("def").func("VP_liquid_2").set("fununit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func("VP_liquid_2").set("argunit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func().create("rho_liquid_2", "Piecewise");
    model.component("comp1").material("mat5").propertyGroup("def").func("rho_liquid_2")
         .set("funcname", "rho_liquid_2");
    model.component("comp1").material("mat5").propertyGroup("def").func("rho_liquid_2").set("arg", "T");
    model.component("comp1").material("mat5").propertyGroup("def").func("rho_liquid_2").set("extrap", "constant");
    model.component("comp1").material("mat5").propertyGroup("def").func("rho_liquid_2")
         .set("pieces", new String[][]{{"273.0", "283.0", "972.7584+0.2084*T^1-4.0E-4*T^2"}, {"283.0", "373.0", "345.28+5.749816*T^1-0.0157244*T^2+1.264375E-5*T^3"}});
    model.component("comp1").material("mat5").propertyGroup("def").func("rho_liquid_2").label("Piecewise 5");
    model.component("comp1").material("mat5").propertyGroup("def").func("rho_liquid_2").set("fununit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func("rho_liquid_2").set("argunit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func().create("TD_liquid_2", "Piecewise");
    model.component("comp1").material("mat5").propertyGroup("def").func("TD_liquid_2")
         .set("funcname", "TD_liquid_2");
    model.component("comp1").material("mat5").propertyGroup("def").func("TD_liquid_2").set("arg", "T");
    model.component("comp1").material("mat5").propertyGroup("def").func("TD_liquid_2").set("extrap", "constant");
    model.component("comp1").material("mat5").propertyGroup("def").func("TD_liquid_2")
         .set("pieces", new String[][]{{"273.0", "333.0", "8.04E-8+2.0E-10*T^1"}});
    model.component("comp1").material("mat5").propertyGroup("def").func("TD_liquid_2").label("Piecewise 6");
    model.component("comp1").material("mat5").propertyGroup("def").func("TD_liquid_2").set("fununit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func("TD_liquid_2").set("argunit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func().create("eta_liquid_1", "Piecewise");
    model.component("comp1").material("mat5").propertyGroup("def").func("eta_liquid_1")
         .set("funcname", "eta_liquid_1");
    model.component("comp1").material("mat5").propertyGroup("def").func("eta_liquid_1").set("arg", "T");
    model.component("comp1").material("mat5").propertyGroup("def").func("eta_liquid_1").set("extrap", "constant");
    model.component("comp1").material("mat5").propertyGroup("def").func("eta_liquid_1")
         .set("pieces", new String[][]{{"265.0", "293.0", "5.948859-0.08236196*T^1+4.287142E-4*T^2-9.938045E-7*T^3+8.65316E-10*T^4"}, {"293.0", "353.0", "0.410191-0.004753985*T^1+2.079795E-5*T^2-4.061698E-8*T^3+2.983925E-11*T^4"}, {"353.0", "423.0", "0.03625638-3.265463E-4*T^1+1.127139E-6*T^2-1.75363E-9*T^3+1.033976E-12*T^4"}});
    model.component("comp1").material("mat5").propertyGroup("def").func("eta_liquid_1").label("Piecewise 7");
    model.component("comp1").material("mat5").propertyGroup("def").func("eta_liquid_1").set("fununit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func("eta_liquid_1").set("argunit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func().create("SurfF", "Piecewise");
    model.component("comp1").material("mat5").propertyGroup("def").func("SurfF").set("funcname", "SurfF");
    model.component("comp1").material("mat5").propertyGroup("def").func("SurfF").set("arg", "T");
    model.component("comp1").material("mat5").propertyGroup("def").func("SurfF").set("extrap", "constant");
    model.component("comp1").material("mat5").propertyGroup("def").func("SurfF")
         .set("pieces", new String[][]{{"274.0", "568.0", "0.07739832+2.071003E-4*T^1-1.23147E-6*T^2+2.344068E-9*T^3-3.033107E-12*T^4+1.758716E-15*T^5"}, {"568.0", "647.0", "-109.3735+0.9291756*T^1-0.003154159*T^2+5.351438E-6*T^3-4.539449E-9*T^4+1.540327E-12*T^5"}});
    model.component("comp1").material("mat5").propertyGroup("def").func("SurfF").label("Piecewise 8");
    model.component("comp1").material("mat5").propertyGroup("def").func("SurfF").set("fununit", "");
    model.component("comp1").material("mat5").propertyGroup("def").func("SurfF").set("argunit", "");
    model.component("comp1").material("mat5").propertyGroup("def").addInput("temperature");
    model.component("comp1").material("mat5").propertyGroup("def").addInput("strainreferencetemperature");
    model.component("comp1").material("mat5").propertyGroup().create("ThermalExpansion", "Thermal expansion");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion")
         .set("dL", "(dL_liquid_2(T[1/K])-dL_liquid_2(Tempref[1/K]))/(1+dL_liquid_2(Tempref[1/K]))");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion")
         .set("dLIso", "(dL_liquid_2(T[1/K])-dL_liquid_2(Tempref[1/K]))/(1+dL_liquid_2(Tempref[1/K]))");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion")
         .set("alphatan", "CTE_liquid_2(T[1/K])[1/K]");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion")
         .set("alphatanIso", "CTE_liquid_2(T[1/K])[1/K]");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").func()
         .create("dL_liquid_2", "Piecewise");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").func("dL_liquid_2")
         .set("funcname", "dL_liquid_2");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").func("dL_liquid_2").set("arg", "T");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").func("dL_liquid_2")
         .set("extrap", "constant");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").func("dL_liquid_2")
         .set("pieces", new String[][]{{"273.0", "283.0", "0.008486158-6.947021E-5*T^1+1.333373E-7*T^2"}, {"283.0", "373.0", "0.232446637-0.002030447*T^1+5.510259E-6*T^2-4.395999E-9*T^3"}});
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").func("dL_liquid_2")
         .label("Piecewise");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").func("dL_liquid_2")
         .set("fununit", "");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").func("dL_liquid_2")
         .set("argunit", "");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").func()
         .create("CTE_liquid_2", "Piecewise");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").func("CTE_liquid_2")
         .set("funcname", "CTE_liquid_2");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").func("CTE_liquid_2").set("arg", "T");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").func("CTE_liquid_2")
         .set("extrap", "constant");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").func("CTE_liquid_2")
         .set("pieces", new String[][]{{"273.0", "283.0", "-6.947321E-5+2.666746E-7*T^1"}, {"283.0", "293.0", "-0.01363715+8.893977E-5*T^1-1.43925E-7*T^2"}, {"293.0", "373.0", "-0.00203045+1.102052E-5*T^1-1.3188E-8*T^2"}});
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").func("CTE_liquid_2")
         .label("Piecewise 1");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").func("CTE_liquid_2")
         .set("fununit", "");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").func("CTE_liquid_2")
         .set("argunit", "");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion").addInput("temperature");
    model.component("comp1").material("mat5").propertyGroup("ThermalExpansion")
         .addInput("strainreferencetemperature");
    model.component("comp1").material("mat1").selection().set(2);
    model.component("comp1").material("mat2").selection().all();

    model.component("comp1").view("view1").set("renderwireframe", true);

    model.component("comp1").material("mat2").selection().set(3);
    model.component("comp1").material("mat2").propertyGroup("def").set("heatcapacity", new String[]{"1387"});
    model.component("comp1").material("mat3").selection().set(1);

    model.func().create("rect1", "Rectangle");

    model.component("comp1").variable().create("var1");
    model.component("comp1").variable("var1").set("T_PMMA", "intop_vol(T)/intop_vol(1)");
    model.component("comp1").variable("var1").descr("T_PMMA", "");
    model.component("comp1").variable("var1").set("t_shifted", "(t-0.5*pulse_width)/(pulse_width)");
    model.component("comp1").variable("var1").descr("t_shifted", "");
    model.component("comp1").variable("var1").set("rect_test", "rect1((t-0.5*pulse_width)/(pulse_width))");
    model.component("comp1").variable("var1").descr("rect_test", "");
    model.component("comp1").variable("var1").set("step_test", "step1(t/pulse_width)+step2(t/pulse_width)");
    model.component("comp1").variable("var1").descr("step_test", "");
    model.component("comp1").variable("var1")
         .set("dissipated_power_medium", "surface_medium(nx*ht.tfluxx+ny*ht.tfluxy+nz*ht.tfluxz)");
    model.component("comp1").variable("var1").descr("dissipated_power_medium", "");
    model.component("comp1").variable("var1")
         .set("dissipated_power_substrate", "-surface_substrate(nx*ht.tfluxx+ny*ht.tfluxy+nz*ht.tfluxz)");
    model.component("comp1").variable("var1").descr("dissipated_power_substrate", "");
    model.component("comp1").variable("var1").set("delta_T", "T-293.15");
    model.component("comp1").variable("var1").descr("delta_T", "");
    model.component("comp1").variable("var1").set("delta_T_PMMA", "T_PMMA-293.15");
    model.component("comp1").variable("var1").descr("delta_T_PMMA", "");
    model.component("comp1").variable("var1").set("tri_test", "tri1(t_shifted)");
    model.component("comp1").variable("var1").descr("tri_test", "");
    model.component("comp1").variable("var1").remove("tri_test");

    model.component("comp1").selection().create("sel1", "Explicit");
    model.component("comp1").selection("sel1").set(3);
    model.component("comp1").selection("sel1").label("nanoparticle");
    model.component("comp1").selection().create("sel2", "Explicit");
    model.component("comp1").selection("sel2").label("nanoparticle surface");
    model.component("comp1").selection("sel2").set(3);
    model.component("comp1").selection("sel2").geom("geom1", 3, 2, new String[]{"exterior"});
    model.component("comp1").selection("sel2").set(3);

    model.component("comp1").cpl().create("intop1", "Integration");
    model.component("comp1").cpl("intop1").set("axisym", true);
    model.component("comp1").cpl("intop1").set("opname", "intop_vol");
    model.component("comp1").cpl("intop1").selection().named("sel1");
    model.component("comp1").cpl().create("intop2", "Integration");
    model.component("comp1").cpl("intop2").set("axisym", true);
    model.component("comp1").cpl("intop2").set("opname", "intop_surf");
    model.component("comp1").cpl("intop2").selection().geom("geom1", 2);
    model.component("comp1").cpl("intop2").selection().named("sel2");
    model.component("comp1").cpl().create("intop3", "Integration");
    model.component("comp1").cpl("intop3").set("axisym", true);
    model.component("comp1").cpl("intop3").set("opname", "surface_medium");
    model.component("comp1").cpl("intop3").label("surface_medium");
    model.component("comp1").cpl().create("intop4", "Integration");
    model.component("comp1").cpl("intop4").set("axisym", true);
    model.component("comp1").cpl("intop4").label("surface_substrate");
    model.component("comp1").cpl("intop4").set("opname", "surface_substrate");
    model.component("comp1").cpl("intop4").selection().geom("geom1", 2);
    model.component("comp1").cpl("intop4").selection().set(14);
    model.component("comp1").cpl("intop3").selection().named("sel1");
    model.component("comp1").cpl("intop3").selection().geom("geom1", 2);
    model.component("comp1").cpl("intop3").selection().named("sel2");
    model.component("comp1").cpl("intop3").selection().geom("geom1", 2);
    model.component("comp1").cpl("intop3").selection().set(10, 11, 12, 13, 15, 16, 17, 18);

    model.param().set("pulse_width", "1000[ns]");
    model.param().descr("pulse_width", "");
    model.param().set("repe_rate", "150[kHz]");
    model.param().descr("repe_rate", "");
    model.param().set("ave_power", "10 [mW]");
    model.param().descr("ave_power", "");
    model.param().set("peak_power", "ave_power/pulse_width/repe_rate");
    model.param().descr("peak_power", "");
    model.param().set("pulse_width", "980[ns]");
    model.param().descr("pulse_width", "IR pulse duration");
    model.param().descr("repe_rate", "IR rep rate");
    model.param().descr("ave_power", "IR average power");
    model.param().descr("peak_power", "IR peak power");
    model.param().set("focal_spot_r", "20[um]");
    model.param().descr("focal_spot_r", "");
    model.param().set("focal_spot_s", "pi*focal_spot_r^2");
    model.param().descr("focal_spot_s", "");
    model.param().descr("focal_spot_r", "IR focal spot radius");
    model.param().descr("focal_spot_s", "IR focal spot area");
    model.param().set("I_ir", "peak_power/focal_spot_s");
    model.param().descr("I_ir", "");
    model.param().descr("I_ir", "IR intensity");
    model.param().descr("r_contact", "Particle substrate contact height");
    model.param().descr("r_PMMA", "Particle radius");
    model.param().set("sigma_abs", "42040 [nm^2]");
    model.param().descr("sigma_abs", "Absoption cross section");
    model.param().descr("sigma_abs", "Absorption cross section");
    model.param().set("volume_heat", "sigma_abs*I_ir/V_PMMA");
    model.param().set("V_PMMA", "4/3*pi*r_PMMA^3");
    model.param().descr("V_PMMA", "");
    model.param().descr("V_PMMA", "Particle volume");
    model.param().descr("volume_heat", "Heat source");
    model.param().set("Q_in", "sigma_abs*I_ir");
    model.param().descr("Q_in", "Heat source");
    model.param().descr("volume_heat", "Volume heat source");
    model.param().set("PMMA_rho", "1190 [kg/m^3]");
    model.param().descr("PMMA_rho", "");

    return model;
  }

  public static Model run3(Model model) {
    model.param().set("PMMA_cp", "1387 [J/(kg*K)]");
    model.param().descr("PMMA_cp", "");

    model.component("comp1").physics("ht").create("hs1", "HeatSource", 3);
    model.component("comp1").physics("ht").feature("hs1").selection().set(3);
    model.component("comp1").physics("ht").feature("hs1").selection().named("sel1");
    model.component("comp1").physics("ht").feature("hs1").set("Q0", "heat_sou*rect_test");
    model.component("comp1").physics("ht").feature("hs1").set("heatSourceType", "GeneralSource");
    model.component("comp1").physics("ht").feature("hs1").set("Q0", "volume_heat*rect_test");
    model.component("comp1").physics("ht").create("temp1", "TemperatureBoundary", 2);
    model.component("comp1").physics("ht").feature("temp1").selection().all();
    model.component("comp1").physics("ht").feature("temp1").selection().set(1, 2, 3, 4, 5, 7, 8, 9, 19, 20);

    model.component("comp1").mesh("mesh1").autoMeshSize(4);
    model.component("comp1").mesh("mesh1").run();

    model.sol().create("sol1");
    model.sol("sol1").study("std1");

    model.study("std1").feature("stat").set("notlistsolnum", 1);
    model.study("std1").feature("stat").set("notsolnum", "1");
    model.study("std1").feature("stat").set("listsolnum", 1);
    model.study("std1").feature("stat").set("solnum", "1");
    model.study("std1").feature("time").set("notlistsolnum", 1);
    model.study("std1").feature("time").set("notsolnum", "auto");
    model.study("std1").feature("time").set("listsolnum", 1);
    model.study("std1").feature("time").set("solnum", "auto");

    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("dtech", "auto");
    model.sol("sol1").feature("s1").feature("fc1").set("initstep", 0.01);
    model.sol("sol1").feature("s1").feature("fc1").set("minstep", 1.0E-6);
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 50);
    model.sol("sol1").feature("s1").feature("fc1").set("termonres", "off");
    model.sol("sol1").feature("s1").create("d1", "Direct");
    model.sol("sol1").feature("s1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol1").feature("s1").feature("d1").set("pivotperturb", 1.0E-13);
    model.sol("sol1").feature("s1").feature("d1").label("Direct, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("s1").create("i1", "Iterative");
    model.sol("sol1").feature("s1").feature("i1").set("linsolver", "gmres");
    model.sol("sol1").feature("s1").feature("i1").set("prefuntype", "left");
    model.sol("sol1").feature("s1").feature("i1").set("rhob", 400);
    model.sol("sol1").feature("s1").feature("i1").label("AMG, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("s1").feature("i1").create("mg1", "Multigrid");
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").set("prefun", "saamg");
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").set("usesmooth", false);
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").set("saamgcompwise", true);
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").feature("pr").create("so1", "SOR");
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").feature("pr").feature("so1").set("iter", 2);
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").feature("pr").feature("so1").set("relax", 0.9);
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").feature("po").create("so1", "SOR");
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").feature("po").feature("so1").set("iter", 2);
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").feature("po").feature("so1").set("relax", 0.9);
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").feature("cs").create("d1", "Direct");
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").feature("cs").feature("d1")
         .set("linsolver", "pardiso");
    model.sol("sol1").feature("s1").create("i2", "Iterative");
    model.sol("sol1").feature("s1").feature("i2").set("linsolver", "gmres");
    model.sol("sol1").feature("s1").feature("i2").set("prefuntype", "left");
    model.sol("sol1").feature("s1").feature("i2").set("rhob", 20);
    model.sol("sol1").feature("s1").feature("i2").label("GMG, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("s1").feature("i2").create("mg1", "Multigrid");
    model.sol("sol1").feature("s1").feature("i2").feature("mg1").set("prefun", "gmg");
    model.sol("sol1").feature("s1").feature("i2").feature("mg1").set("mcasegen", "any");
    model.sol("sol1").feature("s1").feature("i2").feature("mg1").feature("pr").create("so1", "SOR");
    model.sol("sol1").feature("s1").feature("i2").feature("mg1").feature("pr").feature("so1").set("iter", 2);
    model.sol("sol1").feature("s1").feature("i2").feature("mg1").feature("po").create("so1", "SOR");
    model.sol("sol1").feature("s1").feature("i2").feature("mg1").feature("po").feature("so1").set("iter", 2);
    model.sol("sol1").feature("s1").feature("i2").feature("mg1").feature("cs").create("d1", "Direct");
    model.sol("sol1").feature("s1").feature("i2").feature("mg1").feature("cs").feature("d1")
         .set("linsolver", "pardiso");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "d1");
    model.sol("sol1").feature("s1").feature("fc1").set("dtech", "auto");
    model.sol("sol1").feature("s1").feature("fc1").set("initstep", 0.01);
    model.sol("sol1").feature("s1").feature("fc1").set("minstep", 1.0E-6);
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 50);
    model.sol("sol1").feature("s1").feature("fc1").set("termonres", "off");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").create("su1", "StoreSolution");
    model.sol("sol1").create("st2", "StudyStep");
    model.sol("sol1").feature("st2").set("study", "std1");
    model.sol("sol1").feature("st2").set("studystep", "time");
    model.sol("sol1").create("v2", "Variables");
    model.sol("sol1").feature("v2").set("initmethod", "sol");
    model.sol("sol1").feature("v2").set("initsol", "sol1");
    model.sol("sol1").feature("v2").set("initsoluse", "su1");
    model.sol("sol1").feature("v2").set("notsolmethod", "sol");
    model.sol("sol1").feature("v2").set("notsol", "sol1");
    model.sol("sol1").feature("v2").set("control", "time");
    model.sol("sol1").create("t1", "Time");
    model.sol("sol1").feature("t1").set("tlist", "range(0,0.1,1)");
    model.sol("sol1").feature("t1").set("plot", "off");
    model.sol("sol1").feature("t1").set("plotgroup", "Default");
    model.sol("sol1").feature("t1").set("plotfreq", "tout");
    model.sol("sol1").feature("t1").set("probesel", "all");
    model.sol("sol1").feature("t1").set("probes", new String[]{});
    model.sol("sol1").feature("t1").set("probefreq", "tsteps");
    model.sol("sol1").feature("t1").set("atolglobalvaluemethod", "factor");
    model.sol("sol1").feature("t1").set("atolmethod", new String[]{"comp1_T", "global"});
    model.sol("sol1").feature("t1").set("atol", new String[]{"comp1_T", "1e-3"});
    model.sol("sol1").feature("t1").set("atolvaluemethod", new String[]{"comp1_T", "factor"});
    model.sol("sol1").feature("t1").set("estrat", "exclude");
    model.sol("sol1").feature("t1").set("maxorder", 2);
    model.sol("sol1").feature("t1").set("control", "time");
    model.sol("sol1").feature("t1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("t1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("t1").feature("fc1").set("damp", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("maxiter", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("stabacc", "aacc");
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdim", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccmix", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdelay", 1);
    model.sol("sol1").feature("t1").create("d1", "Direct");
    model.sol("sol1").feature("t1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol1").feature("t1").feature("d1").set("pivotperturb", 1.0E-13);
    model.sol("sol1").feature("t1").feature("d1").label("Direct, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("t1").create("i1", "Iterative");
    model.sol("sol1").feature("t1").feature("i1").set("linsolver", "gmres");
    model.sol("sol1").feature("t1").feature("i1").set("prefuntype", "left");
    model.sol("sol1").feature("t1").feature("i1").set("rhob", 400);
    model.sol("sol1").feature("t1").feature("i1").label("AMG, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("t1").feature("i1").create("mg1", "Multigrid");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").set("prefun", "saamg");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").set("usesmooth", false);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").set("saamgcompwise", true);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("pr").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("pr").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("pr").feature("so1").set("relax", 0.9);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("po").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("po").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("po").feature("so1").set("relax", 0.9);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("cs").create("d1", "Direct");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("cs").feature("d1")
         .set("linsolver", "pardiso");
    model.sol("sol1").feature("t1").create("i2", "Iterative");
    model.sol("sol1").feature("t1").feature("i2").set("linsolver", "gmres");
    model.sol("sol1").feature("t1").feature("i2").set("prefuntype", "left");
    model.sol("sol1").feature("t1").feature("i2").set("rhob", 20);
    model.sol("sol1").feature("t1").feature("i2").label("GMG, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("t1").feature("i2").create("mg1", "Multigrid");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").set("prefun", "gmg");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").set("mcasegen", "any");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("pr").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("pr").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("po").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("po").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("cs").create("d1", "Direct");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("cs").feature("d1")
         .set("linsolver", "pardiso");
    model.sol("sol1").feature("t1").feature("fc1").set("linsolver", "d1");
    model.sol("sol1").feature("t1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("t1").feature("fc1").set("damp", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("maxiter", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("stabacc", "aacc");
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdim", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccmix", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdelay", 1);
    model.sol("sol1").feature("t1").feature().remove("fcDef");
    model.sol("sol1").feature("v2").set("notsolnum", "auto");
    model.sol("sol1").feature("v2").set("notsolvertype", "solnum");
    model.sol("sol1").feature("v2").set("notlistsolnum", new String[]{"1"});
    model.sol("sol1").feature("v2").set("notsolnum", "auto");
    model.sol("sol1").feature("v2").set("notlistsolnum", new String[]{"1"});
    model.sol("sol1").feature("v2").set("notsolnum", "auto");
    model.sol("sol1").feature("v2").set("control", "time");
    model.sol("sol1").attach("std1");

    model.result().create("pg1", "PlotGroup3D");
    model.result("pg1").label("Temperature (ht)");
    model.result("pg1").set("data", "dset1");
    model.result("pg1").feature().create("surf1", "Surface");
    model.result("pg1").feature("surf1").label("Surface");
    model.result("pg1").feature("surf1").set("colortable", "ThermalLight");
    model.result("pg1").feature("surf1").set("data", "parent");
    model.result().create("pg2", "PlotGroup3D");
    model.result("pg2").label("Isothermal Contours (ht)");
    model.result("pg2").set("data", "dset1");
    model.result("pg2").feature().create("iso1", "Isosurface");
    model.result("pg2").feature("iso1").label("Isosurface");
    model.result("pg2").feature("iso1").set("number", 10);
    model.result("pg2").feature("iso1").set("levelrounding", false);
    model.result("pg2").feature("iso1").set("colortable", "ThermalLight");
    model.result("pg2").feature("iso1").set("data", "parent");
    model.result().remove("pg2");
    model.result().remove("pg1");

    model.study("std1").feature("stat").active(false);

    model.sol("sol1").study("std1");
    model.sol("sol2").copySolution("sol3");

    model.result().dataset("dset2").set("solution", "none");

    model.study("std1").feature("time").set("notlistsolnum", 1);
    model.study("std1").feature("time").set("notsolnum", "1");
    model.study("std1").feature("time").set("listsolnum", 1);
    model.study("std1").feature("time").set("solnum", "1");

    model.sol("sol1").feature().remove("t1");
    model.sol("sol1").feature().remove("v2");
    model.sol("sol1").feature().remove("st2");
    model.sol("sol1").feature().remove("su1");
    model.sol("sol1").feature().remove("s1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol3").copySolution("sol2");
    model.sol().remove("sol3");
    model.sol("sol2").label("Solution Store 1");

    model.result().dataset().remove("dset4");

    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "time");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "time");
    model.sol("sol1").create("t1", "Time");
    model.sol("sol1").feature("t1").set("tlist", "range(0,0.1,1)");
    model.sol("sol1").feature("t1").set("plot", "off");
    model.sol("sol1").feature("t1").set("plotgroup", "Default");
    model.sol("sol1").feature("t1").set("plotfreq", "tout");
    model.sol("sol1").feature("t1").set("probesel", "all");
    model.sol("sol1").feature("t1").set("probes", new String[]{});
    model.sol("sol1").feature("t1").set("probefreq", "tsteps");
    model.sol("sol1").feature("t1").set("atolglobalvaluemethod", "factor");
    model.sol("sol1").feature("t1").set("atolmethod", new String[]{"comp1_T", "global"});
    model.sol("sol1").feature("t1").set("atol", new String[]{"comp1_T", "1e-3"});
    model.sol("sol1").feature("t1").set("atolvaluemethod", new String[]{"comp1_T", "factor"});
    model.sol("sol1").feature("t1").set("estrat", "exclude");
    model.sol("sol1").feature("t1").set("maxorder", 2);
    model.sol("sol1").feature("t1").set("control", "time");
    model.sol("sol1").feature("t1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("t1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("t1").feature("fc1").set("damp", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("maxiter", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("stabacc", "aacc");
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdim", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccmix", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdelay", 1);
    model.sol("sol1").feature("t1").create("d1", "Direct");
    model.sol("sol1").feature("t1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol1").feature("t1").feature("d1").set("pivotperturb", 1.0E-13);
    model.sol("sol1").feature("t1").feature("d1").label("Direct, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("t1").create("i1", "Iterative");
    model.sol("sol1").feature("t1").feature("i1").set("linsolver", "gmres");
    model.sol("sol1").feature("t1").feature("i1").set("prefuntype", "left");
    model.sol("sol1").feature("t1").feature("i1").set("rhob", 400);
    model.sol("sol1").feature("t1").feature("i1").label("AMG, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("t1").feature("i1").create("mg1", "Multigrid");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").set("prefun", "saamg");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").set("usesmooth", false);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").set("saamgcompwise", true);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("pr").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("pr").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("pr").feature("so1").set("relax", 0.9);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("po").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("po").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("po").feature("so1").set("relax", 0.9);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("cs").create("d1", "Direct");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("cs").feature("d1")
         .set("linsolver", "pardiso");
    model.sol("sol1").feature("t1").create("i2", "Iterative");
    model.sol("sol1").feature("t1").feature("i2").set("linsolver", "gmres");
    model.sol("sol1").feature("t1").feature("i2").set("prefuntype", "left");
    model.sol("sol1").feature("t1").feature("i2").set("rhob", 20);
    model.sol("sol1").feature("t1").feature("i2").label("GMG, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("t1").feature("i2").create("mg1", "Multigrid");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").set("prefun", "gmg");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").set("mcasegen", "any");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("pr").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("pr").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("po").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("po").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("cs").create("d1", "Direct");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("cs").feature("d1")
         .set("linsolver", "pardiso");
    model.sol("sol1").feature("t1").feature("fc1").set("linsolver", "d1");
    model.sol("sol1").feature("t1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("t1").feature("fc1").set("damp", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("maxiter", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("stabacc", "aacc");
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdim", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccmix", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdelay", 1);
    model.sol("sol1").feature("t1").feature().remove("fcDef");

    model.result().dataset("dset2").set("solution", "sol2");

    model.sol().remove("sol2");
    model.sol("sol1").attach("std1");

    model.result().create("pg1", "PlotGroup3D");
    model.result("pg1").label("Temperature (ht)");
    model.result("pg1").set("showlooplevel", new String[]{"off", "off", "off"});
    model.result("pg1").set("data", "dset1");
    model.result("pg1").feature().create("surf1", "Surface");
    model.result("pg1").feature("surf1").label("Surface");
    model.result("pg1").feature("surf1").set("colortable", "ThermalLight");
    model.result("pg1").feature("surf1").set("data", "parent");
    model.result().create("pg2", "PlotGroup3D");
    model.result("pg2").label("Isothermal Contours (ht)");
    model.result("pg2").set("showlooplevel", new String[]{"off", "off", "off"});
    model.result("pg2").set("data", "dset1");
    model.result("pg2").feature().create("iso1", "Isosurface");
    model.result("pg2").feature("iso1").label("Isosurface");
    model.result("pg2").feature("iso1").set("number", 10);
    model.result("pg2").feature("iso1").set("levelrounding", false);
    model.result("pg2").feature("iso1").set("colortable", "ThermalLight");
    model.result("pg2").feature("iso1").set("data", "parent");

    model.sol("sol1").runAll();

    model.result("pg1").run();
    model.result("pg1").run();
    model.result().numerical().create("gev1", "EvalGlobal");
    model.result().numerical("gev1").setIndex("expr", "T_PMMA", 0);
    model.result().table().create("tbl1", "Table");
    model.result().table("tbl1").comments("Global Evaluation 1");
    model.result().numerical("gev1").set("table", "tbl1");
    model.result().numerical("gev1").setResult();
    model.result().create("pg3", "PlotGroup1D");
    model.result("pg3").set("data", "none");
    model.result("pg3").create("tblp1", "Table");
    model.result("pg3").feature("tblp1").set("source", "table");
    model.result("pg3").feature("tblp1").set("table", "tbl1");
    model.result("pg3").run();
    model.result().table().remove("tbl1");

    model.study("std1").feature("time").set("tunit", "ns");
    model.study("std1").feature("time").set("tlist", "range(0,20,3000)");

    model.sol("sol1").study("std1");

    model.study("std1").feature("time").set("notlistsolnum", 1);
    model.study("std1").feature("time").set("notsolnum", "1");
    model.study("std1").feature("time").set("listsolnum", 1);
    model.study("std1").feature("time").set("solnum", "1");

    model.sol("sol1").feature().remove("t1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "time");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "time");
    model.sol("sol1").create("t1", "Time");
    model.sol("sol1").feature("t1").set("tlist", "range(0,20,3000)");
    model.sol("sol1").feature("t1").set("plot", "off");
    model.sol("sol1").feature("t1").set("plotgroup", "pg1");
    model.sol("sol1").feature("t1").set("plotfreq", "tout");
    model.sol("sol1").feature("t1").set("probesel", "all");
    model.sol("sol1").feature("t1").set("probes", new String[]{});
    model.sol("sol1").feature("t1").set("probefreq", "tsteps");
    model.sol("sol1").feature("t1").set("atolglobalvaluemethod", "factor");
    model.sol("sol1").feature("t1").set("atolmethod", new String[]{"comp1_T", "global"});
    model.sol("sol1").feature("t1").set("atol", new String[]{"comp1_T", "1e-3"});
    model.sol("sol1").feature("t1").set("atolvaluemethod", new String[]{"comp1_T", "factor"});
    model.sol("sol1").feature("t1").set("estrat", "exclude");
    model.sol("sol1").feature("t1").set("maxorder", 2);
    model.sol("sol1").feature("t1").set("control", "time");
    model.sol("sol1").feature("t1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("t1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("t1").feature("fc1").set("damp", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("maxiter", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("stabacc", "aacc");
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdim", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccmix", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdelay", 1);
    model.sol("sol1").feature("t1").create("d1", "Direct");
    model.sol("sol1").feature("t1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol1").feature("t1").feature("d1").set("pivotperturb", 1.0E-13);
    model.sol("sol1").feature("t1").feature("d1").label("Direct, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("t1").create("i1", "Iterative");
    model.sol("sol1").feature("t1").feature("i1").set("linsolver", "gmres");
    model.sol("sol1").feature("t1").feature("i1").set("prefuntype", "left");
    model.sol("sol1").feature("t1").feature("i1").set("rhob", 400);
    model.sol("sol1").feature("t1").feature("i1").label("AMG, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("t1").feature("i1").create("mg1", "Multigrid");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").set("prefun", "saamg");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").set("usesmooth", false);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").set("saamgcompwise", true);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("pr").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("pr").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("pr").feature("so1").set("relax", 0.9);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("po").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("po").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("po").feature("so1").set("relax", 0.9);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("cs").create("d1", "Direct");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("cs").feature("d1")
         .set("linsolver", "pardiso");
    model.sol("sol1").feature("t1").create("i2", "Iterative");
    model.sol("sol1").feature("t1").feature("i2").set("linsolver", "gmres");
    model.sol("sol1").feature("t1").feature("i2").set("prefuntype", "left");
    model.sol("sol1").feature("t1").feature("i2").set("rhob", 20);
    model.sol("sol1").feature("t1").feature("i2").label("GMG, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("t1").feature("i2").create("mg1", "Multigrid");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").set("prefun", "gmg");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").set("mcasegen", "any");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("pr").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("pr").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("po").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("po").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("cs").create("d1", "Direct");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("cs").feature("d1")
         .set("linsolver", "pardiso");
    model.sol("sol1").feature("t1").feature("fc1").set("linsolver", "d1");
    model.sol("sol1").feature("t1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("t1").feature("fc1").set("damp", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("maxiter", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("stabacc", "aacc");
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdim", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccmix", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdelay", 1);
    model.sol("sol1").feature("t1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg1").run();
    model.result().table().create("tbl1", "Table");
    model.result().table("tbl1").comments("Global Evaluation 1");
    model.result().numerical("gev1").set("table", "tbl1");
    model.result().numerical("gev1").setResult();
    model.result().create("pg3", "PlotGroup1D");
    model.result("pg3").set("data", "none");
    model.result("pg3").create("tblp1", "Table");
    model.result("pg3").feature("tblp1").set("source", "table");
    model.result("pg3").feature("tblp1").set("table", "tbl1");
    model.result("pg3").run();
    model.result().create("pg4", "PlotGroup3D");
    model.result("pg4").run();
    model.result("pg4").setIndex("looplevel", 74, 0);
    model.result("pg4").run();
    model.result("pg4").run();
    model.result("pg4").create("slc1", "Slice");
    model.result("pg4").feature("slc1").set("expr", "delta_T");
    model.result("pg4").feature("slc1").set("colortable", "Thermal");
    model.result("pg4").run();
    model.result("pg4").feature("slc1").set("quickxnumber", 1);
    model.result("pg4").run();

    model.component("comp1").mesh("mesh1").autoMeshSize(3);
    model.component("comp1").mesh("mesh1").run();
    model.component("comp1").mesh("mesh1").automatic(false);
    model.component("comp1").mesh("mesh1").automatic(true);

    model.study("std1").feature("time").set("tlist", "range(0,10,5000)");

    model.sol("sol1").study("std1");

    model.study("std1").feature("time").set("notlistsolnum", 1);
    model.study("std1").feature("time").set("notsolnum", "1");
    model.study("std1").feature("time").set("listsolnum", 1);
    model.study("std1").feature("time").set("solnum", "1");

    model.sol("sol1").feature().remove("t1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "time");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "time");
    model.sol("sol1").create("t1", "Time");
    model.sol("sol1").feature("t1").set("tlist", "range(0,10,5000)");
    model.sol("sol1").feature("t1").set("plot", "off");
    model.sol("sol1").feature("t1").set("plotgroup", "pg1");
    model.sol("sol1").feature("t1").set("plotfreq", "tout");
    model.sol("sol1").feature("t1").set("probesel", "all");
    model.sol("sol1").feature("t1").set("probes", new String[]{});
    model.sol("sol1").feature("t1").set("probefreq", "tsteps");
    model.sol("sol1").feature("t1").set("atolglobalvaluemethod", "factor");
    model.sol("sol1").feature("t1").set("atolmethod", new String[]{"comp1_T", "global"});
    model.sol("sol1").feature("t1").set("atol", new String[]{"comp1_T", "1e-3"});
    model.sol("sol1").feature("t1").set("atolvaluemethod", new String[]{"comp1_T", "factor"});
    model.sol("sol1").feature("t1").set("estrat", "exclude");

    return model;
  }

  public static Model run4(Model model) {
    model.sol("sol1").feature("t1").set("maxorder", 2);
    model.sol("sol1").feature("t1").set("control", "time");
    model.sol("sol1").feature("t1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("t1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("t1").feature("fc1").set("damp", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("maxiter", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("stabacc", "aacc");
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdim", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccmix", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdelay", 1);
    model.sol("sol1").feature("t1").create("d1", "Direct");
    model.sol("sol1").feature("t1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol1").feature("t1").feature("d1").set("pivotperturb", 1.0E-13);
    model.sol("sol1").feature("t1").feature("d1").label("Direct, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("t1").create("i1", "Iterative");
    model.sol("sol1").feature("t1").feature("i1").set("linsolver", "gmres");
    model.sol("sol1").feature("t1").feature("i1").set("prefuntype", "left");
    model.sol("sol1").feature("t1").feature("i1").set("rhob", 400);
    model.sol("sol1").feature("t1").feature("i1").label("AMG, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("t1").feature("i1").create("mg1", "Multigrid");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").set("prefun", "saamg");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").set("usesmooth", false);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").set("saamgcompwise", true);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("pr").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("pr").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("pr").feature("so1").set("relax", 0.9);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("po").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("po").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("po").feature("so1").set("relax", 0.9);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("cs").create("d1", "Direct");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("cs").feature("d1")
         .set("linsolver", "pardiso");
    model.sol("sol1").feature("t1").create("i2", "Iterative");
    model.sol("sol1").feature("t1").feature("i2").set("linsolver", "gmres");
    model.sol("sol1").feature("t1").feature("i2").set("prefuntype", "left");
    model.sol("sol1").feature("t1").feature("i2").set("rhob", 20);
    model.sol("sol1").feature("t1").feature("i2").label("GMG, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("t1").feature("i2").create("mg1", "Multigrid");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").set("prefun", "gmg");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").set("mcasegen", "any");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("pr").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("pr").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("po").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("po").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("cs").create("d1", "Direct");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("cs").feature("d1")
         .set("linsolver", "pardiso");
    model.sol("sol1").feature("t1").feature("fc1").set("linsolver", "d1");
    model.sol("sol1").feature("t1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("t1").feature("fc1").set("damp", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("maxiter", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("stabacc", "aacc");
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdim", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccmix", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdelay", 1);
    model.sol("sol1").feature("t1").feature().remove("fcDef");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg1").run();
    model.result().table().remove("tbl1");
    model.result().table().create("tbl1", "Table");
    model.result().table("tbl1").comments("Global Evaluation 1");
    model.result().numerical("gev1").set("table", "tbl1");
    model.result().numerical("gev1").setResult();
    model.result().create("pg5", "PlotGroup1D");
    model.result("pg5").set("data", "none");
    model.result("pg5").create("tblp1", "Table");
    model.result("pg5").feature("tblp1").set("source", "table");
    model.result("pg5").feature("tblp1").set("table", "tbl1");
    model.result("pg5").run();
    model.result("pg4").run();
    model.result("pg4").run();
    model.result("pg5").run();

    model.study("std1").feature("time").active(false);
    model.study("std1").feature("time").active(true);
    model.study("std1").feature("stat").active(true);

    model.component("comp1").physics("ht").feature("hs1").set("Q0", "volume_heat");

    model.sol("sol1").study("std1");

    model.study("std1").feature("time").set("notsolnum", "auto");
    model.study("std1").feature("time").set("notsolvertype", "solnum");
    model.study("std1").feature("time").set("notlistsolnum", new String[]{"1"});
    model.study("std1").feature("time").set("notsolnum", "auto");
    model.study("std1").feature("time").set("notsolnumhide", "off");
    model.study("std1").feature("time").set("notstudyhide", "on");
    model.study("std1").feature("time").set("notsolhide", "on");
    model.study("std1").feature("time").set("solnum", "auto");
    model.study("std1").feature("time").set("solvertype", "solnum");
    model.study("std1").feature("time").set("listsolnum", new String[]{"1"});
    model.study("std1").feature("time").set("solnum", "auto");
    model.study("std1").feature("time").set("solnumhide", "off");
    model.study("std1").feature("time").set("initstudyhide", "on");
    model.study("std1").feature("time").set("initsolhide", "on");
    model.study("std1").feature("stat").set("notlistsolnum", 1);
    model.study("std1").feature("stat").set("notsolnum", "1");
    model.study("std1").feature("stat").set("listsolnum", 1);
    model.study("std1").feature("stat").set("solnum", "1");
    model.study("std1").feature("time").set("notlistsolnum", 1);
    model.study("std1").feature("time").set("notsolnum", "auto");
    model.study("std1").feature("time").set("listsolnum", 1);
    model.study("std1").feature("time").set("solnum", "auto");

    model.sol("sol1").feature().remove("t1");
    model.sol("sol1").feature().remove("v1");
    model.sol("sol1").feature().remove("st1");
    model.sol("sol1").create("st1", "StudyStep");
    model.sol("sol1").feature("st1").set("study", "std1");
    model.sol("sol1").feature("st1").set("studystep", "stat");
    model.sol("sol1").create("v1", "Variables");
    model.sol("sol1").feature("v1").set("control", "stat");
    model.sol("sol1").create("s1", "Stationary");
    model.sol("sol1").feature("s1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("s1").feature("fc1").set("dtech", "auto");
    model.sol("sol1").feature("s1").feature("fc1").set("initstep", 0.01);
    model.sol("sol1").feature("s1").feature("fc1").set("minstep", 1.0E-6);
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 50);
    model.sol("sol1").feature("s1").feature("fc1").set("termonres", "off");
    model.sol("sol1").feature("s1").create("d1", "Direct");
    model.sol("sol1").feature("s1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol1").feature("s1").feature("d1").set("pivotperturb", 1.0E-13);
    model.sol("sol1").feature("s1").feature("d1").label("Direct, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("s1").create("i1", "Iterative");
    model.sol("sol1").feature("s1").feature("i1").set("linsolver", "gmres");
    model.sol("sol1").feature("s1").feature("i1").set("prefuntype", "left");
    model.sol("sol1").feature("s1").feature("i1").set("rhob", 400);
    model.sol("sol1").feature("s1").feature("i1").label("AMG, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("s1").feature("i1").create("mg1", "Multigrid");
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").set("prefun", "saamg");
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").set("usesmooth", false);
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").set("saamgcompwise", true);
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").feature("pr").create("so1", "SOR");
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").feature("pr").feature("so1").set("iter", 2);
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").feature("pr").feature("so1").set("relax", 0.9);
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").feature("po").create("so1", "SOR");
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").feature("po").feature("so1").set("iter", 2);
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").feature("po").feature("so1").set("relax", 0.9);
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").feature("cs").create("d1", "Direct");
    model.sol("sol1").feature("s1").feature("i1").feature("mg1").feature("cs").feature("d1")
         .set("linsolver", "pardiso");
    model.sol("sol1").feature("s1").create("i2", "Iterative");
    model.sol("sol1").feature("s1").feature("i2").set("linsolver", "gmres");
    model.sol("sol1").feature("s1").feature("i2").set("prefuntype", "left");
    model.sol("sol1").feature("s1").feature("i2").set("rhob", 20);
    model.sol("sol1").feature("s1").feature("i2").label("GMG, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("s1").feature("i2").create("mg1", "Multigrid");
    model.sol("sol1").feature("s1").feature("i2").feature("mg1").set("prefun", "gmg");
    model.sol("sol1").feature("s1").feature("i2").feature("mg1").set("mcasegen", "any");
    model.sol("sol1").feature("s1").feature("i2").feature("mg1").feature("pr").create("so1", "SOR");
    model.sol("sol1").feature("s1").feature("i2").feature("mg1").feature("pr").feature("so1").set("iter", 2);
    model.sol("sol1").feature("s1").feature("i2").feature("mg1").feature("po").create("so1", "SOR");
    model.sol("sol1").feature("s1").feature("i2").feature("mg1").feature("po").feature("so1").set("iter", 2);
    model.sol("sol1").feature("s1").feature("i2").feature("mg1").feature("cs").create("d1", "Direct");
    model.sol("sol1").feature("s1").feature("i2").feature("mg1").feature("cs").feature("d1")
         .set("linsolver", "pardiso");
    model.sol("sol1").feature("s1").feature("fc1").set("linsolver", "d1");
    model.sol("sol1").feature("s1").feature("fc1").set("dtech", "auto");
    model.sol("sol1").feature("s1").feature("fc1").set("initstep", 0.01);
    model.sol("sol1").feature("s1").feature("fc1").set("minstep", 1.0E-6);
    model.sol("sol1").feature("s1").feature("fc1").set("maxiter", 50);
    model.sol("sol1").feature("s1").feature("fc1").set("termonres", "off");
    model.sol("sol1").feature("s1").feature().remove("fcDef");
    model.sol("sol1").create("su1", "StoreSolution");
    model.sol("sol1").create("st2", "StudyStep");
    model.sol("sol1").feature("st2").set("study", "std1");
    model.sol("sol1").feature("st2").set("studystep", "time");
    model.sol("sol1").create("v2", "Variables");
    model.sol("sol1").feature("v2").set("initmethod", "sol");
    model.sol("sol1").feature("v2").set("initsol", "sol1");
    model.sol("sol1").feature("v2").set("initsoluse", "su1");
    model.sol("sol1").feature("v2").set("notsolmethod", "sol");
    model.sol("sol1").feature("v2").set("notsol", "sol1");
    model.sol("sol1").feature("v2").set("control", "time");
    model.sol("sol1").create("t1", "Time");
    model.sol("sol1").feature("t1").set("tlist", "range(0,10,5000)");
    model.sol("sol1").feature("t1").set("plot", "off");
    model.sol("sol1").feature("t1").set("plotgroup", "pg1");
    model.sol("sol1").feature("t1").set("plotfreq", "tout");
    model.sol("sol1").feature("t1").set("probesel", "all");
    model.sol("sol1").feature("t1").set("probes", new String[]{});
    model.sol("sol1").feature("t1").set("probefreq", "tsteps");
    model.sol("sol1").feature("t1").set("atolglobalvaluemethod", "factor");
    model.sol("sol1").feature("t1").set("atolmethod", new String[]{"comp1_T", "global"});
    model.sol("sol1").feature("t1").set("atol", new String[]{"comp1_T", "1e-3"});
    model.sol("sol1").feature("t1").set("atolvaluemethod", new String[]{"comp1_T", "factor"});
    model.sol("sol1").feature("t1").set("estrat", "exclude");
    model.sol("sol1").feature("t1").set("maxorder", 2);
    model.sol("sol1").feature("t1").set("control", "time");
    model.sol("sol1").feature("t1").create("fc1", "FullyCoupled");
    model.sol("sol1").feature("t1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("t1").feature("fc1").set("damp", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("maxiter", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("stabacc", "aacc");
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdim", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccmix", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdelay", 1);
    model.sol("sol1").feature("t1").create("d1", "Direct");
    model.sol("sol1").feature("t1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol1").feature("t1").feature("d1").set("pivotperturb", 1.0E-13);
    model.sol("sol1").feature("t1").feature("d1").label("Direct, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("t1").create("i1", "Iterative");
    model.sol("sol1").feature("t1").feature("i1").set("linsolver", "gmres");
    model.sol("sol1").feature("t1").feature("i1").set("prefuntype", "left");
    model.sol("sol1").feature("t1").feature("i1").set("rhob", 400);
    model.sol("sol1").feature("t1").feature("i1").label("AMG, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("t1").feature("i1").create("mg1", "Multigrid");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").set("prefun", "saamg");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").set("usesmooth", false);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").set("saamgcompwise", true);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("pr").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("pr").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("pr").feature("so1").set("relax", 0.9);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("po").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("po").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("po").feature("so1").set("relax", 0.9);
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("cs").create("d1", "Direct");
    model.sol("sol1").feature("t1").feature("i1").feature("mg1").feature("cs").feature("d1")
         .set("linsolver", "pardiso");
    model.sol("sol1").feature("t1").create("i2", "Iterative");
    model.sol("sol1").feature("t1").feature("i2").set("linsolver", "gmres");
    model.sol("sol1").feature("t1").feature("i2").set("prefuntype", "left");
    model.sol("sol1").feature("t1").feature("i2").set("rhob", 20);
    model.sol("sol1").feature("t1").feature("i2").label("GMG, Heat Transfer Variables (ht)");
    model.sol("sol1").feature("t1").feature("i2").create("mg1", "Multigrid");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").set("prefun", "gmg");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").set("mcasegen", "any");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("pr").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("pr").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("po").create("so1", "SOR");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("po").feature("so1").set("iter", 2);
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("cs").create("d1", "Direct");
    model.sol("sol1").feature("t1").feature("i2").feature("mg1").feature("cs").feature("d1")
         .set("linsolver", "pardiso");
    model.sol("sol1").feature("t1").feature("fc1").set("linsolver", "d1");
    model.sol("sol1").feature("t1").feature("fc1").set("jtech", "once");
    model.sol("sol1").feature("t1").feature("fc1").set("damp", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("maxiter", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("stabacc", "aacc");
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdim", 5);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccmix", 0.9);
    model.sol("sol1").feature("t1").feature("fc1").set("aaccdelay", 1);
    model.sol("sol1").feature("t1").feature().remove("fcDef");
    model.sol("sol1").feature("v2").set("notsolnum", "auto");
    model.sol("sol1").feature("v2").set("notsolvertype", "solnum");
    model.sol("sol1").feature("v2").set("notlistsolnum", new String[]{"1"});
    model.sol("sol1").feature("v2").set("notsolnum", "auto");
    model.sol("sol1").feature("v2").set("notlistsolnum", new String[]{"1"});
    model.sol("sol1").feature("v2").set("notsolnum", "auto");
    model.sol("sol1").feature("v2").set("control", "time");
    model.sol("sol1").attach("std1");
    model.sol("sol1").runAll();

    model.result("pg1").run();
    model.result().numerical().create("gev2", "EvalGlobal");
    model.result().numerical("gev2").set("data", "dset2");
    model.result().numerical("gev2").setIndex("expr", "T", 0);
    model.result().numerical("gev2").setIndex("expr", "T_PMMA", 0);
    model.result().table().create("tbl2", "Table");
    model.result().table("tbl2").comments("Global Evaluation 2");
    model.result().numerical("gev2").set("table", "tbl2");
    model.result().numerical("gev2").setResult();

    model.study("std1").feature("time").active(false);
    model.study("std1").create("param", "Parametric");
    model.study("std1").feature().remove("param");
    model.study("std1").create("param", "Parametric");
    model.study("std1").feature().remove("param");
    model.study("std1").feature("time").active(true);
    model.study("std1").feature("time").set("useinitsol", true);
    model.study("std1").feature("time").set("initmethod", "init");
    model.study("std1").feature("time").set("useinitsol", false);

    model.result().numerical().create("gev3", "EvalGlobal");
    model.result().numerical("gev3").setIndex("expr", "Q_in/", 0);
    model.result().numerical("gev3").setIndex("expr", "Q_in/delta_T_PMMA", 0);
    model.result().table().create("tbl3", "Table");
    model.result().table("tbl3").comments("Global Evaluation 3");
    model.result().numerical("gev3").set("table", "tbl3");
    model.result().numerical("gev3").setResult();
    model.result().table("tbl3").clearTableData();
    model.result().table().remove("tbl3");
    model.result().numerical("gev3").set("data", "dset2");
    model.result().table().create("tbl3", "Table");
    model.result().table("tbl3").comments("Global Evaluation 3");
    model.result().numerical("gev3").set("table", "tbl3");
    model.result().numerical("gev3").setResult();

    return model;
  }

  public static void main(String[] args) {
    Model model = run();
    model = run2(model);
    model = run3(model);
    run4(model);
  }

}
