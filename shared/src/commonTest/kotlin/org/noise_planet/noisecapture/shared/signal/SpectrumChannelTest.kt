package org.noise_planet.noisecapture.shared.signal

import com.goncalossilva.resources.Resource
import kotlinx.coroutines.test.runTest
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue


class SpectrumChannelTest {


    fun get44100HZ100TO16000() : SpectrumChannelConfiguration {
            return SpectrumChannelConfiguration(
                bandpass=listOf(
                    Bandpass(99.21256574801247,111.36233976754241,88.38834764831843,100.0,2,Sos(a1=doubleArrayOf(-1.9965364131606704,-1.996750453198385,-1.9972639850393383,-1.9977078138459268,-1.9988093943603864,-1.9990875422352576), a2=doubleArrayOf(0.996748293529012,0.9969382790329787,0.9974991288319082,0.9978772006004107,0.999059074482199,0.9992473012061489), b0=doubleArrayOf(1.9096008867615595e-17,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(3.819201773523119e-17,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(1.9096008867615595e-17,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(396.8502629920499,445.44935907016975,353.5533905932738,400.0,sos=Sos(a1=doubleArrayOf(-1.983683760843643,-1.9848177973188836,-1.9862875464023864,-1.9888340386407348,-1.9922540564683868,-1.9944395456030453), a2=doubleArrayOf(0.9870565418362137,0.9878086030156703,0.9900347138507755,0.9915350658113805,0.996242067775943,0.9969922977121467), b0=doubleArrayOf(7.675358798811291e-14,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(1.5350717597622583e-13,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(7.675358798811291e-14,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(125.0,140.30775603867164,111.36233976754241,125.0,2,Sos(a1=doubleArrayOf(-1.9955686463431954,-1.9958459676577474,-1.9964769915778326,-1.9970573610880828,-1.9984183696347848,-1.9987981731888196), a2=doubleArrayOf(0.9959048400703698,0.9961440013871193,0.9968501336181875,0.9973261694669386,0.9988146588348366,0.999051747964678), b0=doubleArrayOf(7.625883439127173e-17,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(1.5251766878254346e-16,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(7.625883439127173e-17,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(500.0,561.2310241546866,445.44935907016963,500.0,sos=Sos(a1=doubleArrayOf(-1.978375713144662,-1.9799242729184654,-1.9815219064760534,-1.9850636820103815,-1.9889417297107825,-1.9921616221501939), a2=doubleArrayOf(0.9837198346904802,0.9846637063121387,0.9874614057534,0.9893460286738955,0.9952679803456205,0.9962117744393044), b0=doubleArrayOf(3.050178389757522e-13,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(6.100356779515044e-13,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(3.050178389757522e-13,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(157.49013123685916,176.7766952966369,140.30775603867164,160.0,2,Sos(a1=doubleArrayOf(-1.9943097900886524,-1.9946713116001737,-1.9954409879611368,-1.9962057847910375,-1.997877844403779,-1.9984029467524744), a2=doubleArrayOf(0.9948431721342493,0.9951441671210961,0.9960330611133081,0.9966323377918527,0.9985068052423528,0.9988054170367806), b0=doubleArrayOf(3.044058450203638e-16,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(6.088116900407276e-16,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(3.044058450203638e-16,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(629.9605249474366,707.1067811865476,561.2310241546866,630.0,sos=Sos(a1=doubleArrayOf(-1.9710687181060893,-1.9732083856639946,-1.97481898635759,-1.9798073097638182,-1.984009403826708,-1.9888042278234839), a2=doubleArrayOf(0.9795319509086966,0.9807151306083703,0.9842293382669933,0.9865943148708757,0.9940424264123358,0.9952290061875053), b0=doubleArrayOf(1.2101032033538562e-12,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(2.4202064067077124e-12,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(1.2101032033538562e-12,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(198.42513149602493,222.72467953508482,176.77669529663686,200.0,2,Sos(a1=doubleArrayOf(-1.9926610639212046,-1.993135746076362,-1.9940652391539457,-1.995082032908412,-1.997120891752752,-1.997856368425131), a2=doubleArrayOf(0.9935071670438599,0.9938858674931755,0.995004585865703,0.995758833577239,0.9981190804995169,0.9984951384537887), b0=doubleArrayOf(1.214460313047029e-15,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(2.428920626094058e-15,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(1.214460313047029e-15,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(793.7005259840998,890.8987181403395,707.1067811865476,800.0,sos=Sos(a1=doubleArrayOf(-1.9608874446844493,-1.963879545064532,-1.965272672649919,-1.9723856664756494,-1.97659489715021,-1.9838025235597203), a2=doubleArrayOf(0.9742810207967747,0.9757617669177364,0.9801733960432047,0.9831371116044616,0.9925011997680455,0.9939916952442285), b0=doubleArrayOf(4.790763704092158e-12,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(9.581527408184316e-12,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(4.790763704092158e-12,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(250.0,280.6155120773433,222.72467953508482,250.0,2,Sos(a1=doubleArrayOf(-1.990484543589923,-1.9911129858369792,-1.9922202506858138,-1.9935856131585172,-1.9960467535953117,-1.9970905797877736), a2=doubleArrayOf(0.9918264632393188,0.9923027422852423,0.9937103364002111,0.9946593406583514,0.9976308150496469,0.9981043341042959), b0=doubleArrayOf(4.841959696459329e-15,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(9.683919392918658e-15,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(4.841959696459329e-15,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(1000.0,1122.4620483093731,890.8987181403393,1000.0,sos=Sos(a1=doubleArrayOf(-1.9465287395721516,-1.9507624588529555,-1.9515135594143518,-1.9617755601288898,-1.9653587645094899,-1.9762804069485385), a2=doubleArrayOf(0.9677055193767125,0.9695545234083117,0.9750893095099044,0.9787962486896891,0.9905642285037186,0.9924340097579212), b0=doubleArrayOf(1.8916509736126712e-11,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(3.7833019472253423e-11,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(1.8916509736126712e-11,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(314.9802624737183,353.5533905932738,280.6155120773433,315.0,2,Sos(a1=doubleArrayOf(-1.9875851916090912,-1.9884250270785415,-1.9897188898480878,-1.9915725553870065,-1.9945024522551325,-1.9960033805175177), a2=doubleArrayOf(0.9897129626266903,0.9903116554314061,0.9920821533812527,0.9932757218314784,0.9970160247033005,0.997612139139008), b0=doubleArrayOf(1.9288175446367492e-14,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(3.8576350892734984e-14,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(1.9288175446367492e-14,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(1259.9210498948732,1414.213562373095,1122.4620483093731,1250.0,sos=Sos(a1=doubleArrayOf(-1.9260424262170373,-1.9320996286683723,-1.9314647877308484,-1.9464276256666304,-1.9482192536879337,-1.9648762820351429), a2=doubleArrayOf(0.959484453052192,0.9617861010981933,0.9687258551697253,0.9733497927864203,0.9881320776548521,0.9904730011558084), b0=doubleArrayOf(7.444638326450626e-11,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(1.4889276652901253e-10,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(7.444638326450626e-11,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(396.8502629920499,445.44935907016975,353.5533905932738,400.0,2,Sos(a1=doubleArrayOf(-1.983683760843643,-1.9848177973188836,-1.9862875464023864,-1.9888340386407348,-1.9922540564683868,-1.9944395456030453), a2=doubleArrayOf(0.9870565418362137,0.9878086030156703,0.9900347138507755,0.9915350658113805,0.996242067775943,0.9969922977121467), b0=doubleArrayOf(7.675358798811291e-14,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(1.5350717597622583e-13,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(7.675358798811291e-14,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(1587.4010519681995,1781.7974362806785,1414.2135623730949,1600.0,sos=Sos(a1=doubleArrayOf(-1.8965027818651838,-1.9052541730305297,-1.9019737635216158,-1.9239887853351916,-1.9219458962629674,-1.9474731148985207), a2=doubleArrayOf(0.9492267296230263,0.95207917414863,0.9607765846022719,0.9665216737012422,0.9850820532134703,0.9880039518087411), b0=doubleArrayOf(2.917804238031064e-10,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(5.835608476062128e-10,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(2.917804238031064e-10,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(500.0,561.2310241546866,445.44935907016963,500.0,2,Sos(a1=doubleArrayOf(-1.978375713144662,-1.9799242729184654,-1.9815219064760534,-1.9850636820103815,-1.9889417297107825,-1.9921616221501939), a2=doubleArrayOf(0.9837198346904802,0.9846637063121387,0.9874614057534,0.9893460286738955,0.9952679803456205,0.9962117744393044), b0=doubleArrayOf(3.050178389757522e-13,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(6.100356779515044e-13,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(3.050178389757522e-13,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(2000.0,2244.9240966187463,1781.7974362806785,2000.0,sos=Sos(a1=doubleArrayOf(-1.853524646230276,-1.8662693215227235,-1.8582666776011347,-1.8908847111532185,-1.881546589134322,-1.9207843344407987), a2=doubleArrayOf(0.9364604437606376,0.9399729439539108,0.9508720272624499,0.9579687210223301,0.9812642472067368,0.9848941893771724), b0=doubleArrayOf(1.1377293181749668e-09,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(2.2754586363499337e-09,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(1.1377293181749668e-09,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(629.9605249474366,707.1067811865476,561.2310241546866,630.0,2,Sos(a1=doubleArrayOf(-1.9710687181060893,-1.9732083856639946,-1.97481898635759,-1.9798073097638182,-1.984009403826708,-1.9888042278234839), a2=doubleArrayOf(0.9795319509086966,0.9807151306083703,0.9842293382669933,0.9865943148708757,0.9940424264123358,0.9952290061875053), b0=doubleArrayOf(1.2101032033538562e-12,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(2.4202064067077124e-12,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(1.2101032033538562e-12,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(2519.8420997897465,2828.42712474619,2244.9240966187463,2500.0,sos=Sos(a1=doubleArrayOf(-1.7905716270112106,-1.809232149768823,-1.7931653070496998,-1.8417048438803578,-1.8193689745652162,-1.8797292043052978), a2=doubleArrayOf(0.9206234986422084,0.9249080336194182,0.9385743411553938,0.9472640245029827,0.9764983624230299,0.9809745604874052), b0=doubleArrayOf(4.408111664350121e-09,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(8.816223328700242e-09,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(4.408111664350121e-09,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(793.7005259840998,890.8987181403395,707.1067811865476,800.0,2,Sos(a1=doubleArrayOf(-1.9608874446844493,-1.963879545064532,-1.965272672649919,-1.9723856664756494,-1.97659489715021,-1.9838025235597203), a2=doubleArrayOf(0.9742810207967747,0.9757617669177364,0.9801733960432047,0.9831371116044616,0.9925011997680455,0.9939916952442285), b0=doubleArrayOf(4.790763704092158e-12,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(9.581527408184316e-12,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(4.790763704092158e-12,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(3174.8021039363994,3563.594872561358,2828.42712474619,3150.0,sos=Sos(a1=doubleArrayOf(-1.698018049211772,-1.7253955989854663,-1.6960230821791877,-1.768329526959257,-1.7238475768878447,-1.8165175010628238), a2=doubleArrayOf(0.9010581254789203,0.9062094836371017,0.9233783287144209,0.9338745051748574,0.9705731974939211,0.9760270510168769), b0=doubleArrayOf(1.6944944571208185e-08,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(3.388988914241637e-08,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(1.6944944571208185e-08,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(1000.0,1122.4620483093731,890.8987181403393,1000.0,2,Sos(a1=doubleArrayOf(-1.9465287395721516,-1.9507624588529555,-1.9515135594143518,-1.9617755601288898,-1.9653587645094899,-1.9762804069485385), a2=doubleArrayOf(0.9677055193767125,0.9695545234083117,0.9750893095099044,0.9787962486896891,0.9905642285037186,0.9924340097579212), b0=doubleArrayOf(1.8916509736126712e-11,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(3.7833019472253423e-11,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(1.8916509736126712e-11,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(4000.0,4489.8481932374925,3563.594872561357,4000.0,sos=Sos(a1=doubleArrayOf(-1.5620018562313807,-1.6020680617209626,-1.5514295488390417,-1.6587733605130148,-1.5779211014645125,-1.7193763872935146), a2=doubleArrayOf(0.8770137966311882,0.8830668098889431,0.9047265072461494,0.9171282371849951,0.9632528802297299,0.9697655086311445), b0=doubleArrayOf(6.450735040335462e-08,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(1.2901470080670925e-07,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(6.450735040335462e-08,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(1259.9210498948732,1414.213562373095,1122.4620483093731,1250.0,2,Sos(a1=doubleArrayOf(-1.9260424262170373,-1.9320996286683723,-1.9314647877308484,-1.9464276256666304,-1.9482192536879337,-1.9648762820351429), a2=doubleArrayOf(0.959484453052192,0.9617861010981933,0.9687258551697253,0.9733497927864203,0.9881320776548521,0.9904730011558084), b0=doubleArrayOf(7.444638326450626e-11,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(1.4889276652901253e-10,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(7.444638326450626e-11,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(5039.684199579493,5656.85424949238,4489.8481932374925,5000.0,sos=Sos(a1=doubleArrayOf(-1.3633369519581289,-1.4214643083634073,-1.3380146172088572,-1.495861751008069,-1.3574610576475574,-1.570954880985224), a2=doubleArrayOf(0.8476665728706247,0.8545076730564747,0.8820537169725038,0.896161432848994,0.954298805785257,0.9618028595678236), b0=doubleArrayOf(2.426697477611551e-07,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(4.853394955223102e-07,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(2.426697477611551e-07,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(1587.4010519681995,1781.7974362806785,1414.2135623730949,1600.0,2,Sos(a1=doubleArrayOf(-1.8965027818651838,-1.9052541730305297,-1.9019737635216158,-1.9239887853351916,-1.9219458962629674,-1.9474731148985207), a2=doubleArrayOf(0.9492267296230263,0.95207917414863,0.9607765846022719,0.9665216737012422,0.9850820532134703,0.9880039518087411), b0=doubleArrayOf(2.917804238031064e-10,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(5.835608476062128e-10,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(2.917804238031064e-10,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(6349.604207872798,7127.189745122714,5656.8542494923795,6300.0,sos=Sos(a1=doubleArrayOf(-1.0773395486328345,-1.1602031096288796,-1.0283985417327675,-1.256274714648876,-1.0309072558826313,-1.3467894695867646), a2=doubleArrayOf(0.8121705498843197,0.8193547936396985,0.8548947631581487,0.869820439177322,0.9435278418933211,0.9515891151377003), b0=doubleArrayOf(8.998148499223296e-07,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(1.7996296998446593e-06,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(8.998148499223296e-07,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(2000.0,2244.9240966187463,1781.7974362806785,2000.0,2,Sos(a1=doubleArrayOf(-1.853524646230276,-1.8662693215227235,-1.8582666776011347,-1.8908847111532185,-1.881546589134322,-1.9207843344407987), a2=doubleArrayOf(0.9364604437606376,0.9399729439539108,0.9508720272624499,0.9579687210223301,0.9812642472067368,0.9848941893771724), b0=doubleArrayOf(1.1377293181749668e-09,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(2.2754586363499337e-09,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(1.1377293181749668e-09,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(8000.0,8979.696386474985,7127.189745122714,8000.0,sos=Sos(a1=doubleArrayOf(-0.6767266245218572,-0.7912873195095531,-0.5929092846277738,-0.9115385133184579,-0.563294468815372,-1.0151634969766976), a2=doubleArrayOf(0.7697779369631321,0.7761347143350338,0.8231381617779963,0.8364515353139432,0.9309588850819872,0.9382780377204392), b0=doubleArrayOf(3.279097356293735e-06,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(6.55819471258747e-06,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(3.279097356293735e-06,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(2519.8420997897465,2828.42712474619,2244.9240966187463,2500.0,2,Sos(a1=doubleArrayOf(-1.7905716270112106,-1.809232149768823,-1.7931653070496998,-1.8417048438803578,-1.8193689745652162,-1.8797292043052978), a2=doubleArrayOf(0.9206234986422084,0.9249080336194182,0.9385743411553938,0.9472640245029827,0.9764983624230299,0.9809745604874052), b0=doubleArrayOf(4.408111664350121e-09,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(8.816223328700242e-09,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(4.408111664350121e-09,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(10079.368399158986,11313.70849898476,8979.696386474985,10000.0,sos=Sos(a1=doubleArrayOf(-0.14222192561841182,-0.29277856105906586,-0.012595844464301289,-0.4348904602476619,0.06824248192606285,-0.5419579256858204), a2=doubleArrayOf(0.7201408449235422,0.7228312366826449,0.7876833682787011,0.7933613741944567,0.9172048817957851,0.9203880227993824), b0=doubleArrayOf(1.170610624091675e-05,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(2.34122124818335e-05,2.0,-2.0,-2.0,2.0,-2.0), b2=doubleArrayOf(1.170610624091675e-05,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(3174.8021039363994,3563.594872561358,2828.42712474619,3150.0,2,Sos(a1=doubleArrayOf(-1.698018049211772,-1.7253955989854663,-1.6960230821791877,-1.768329526959257,-1.7238475768878447,-1.8165175010628238), a2=doubleArrayOf(0.9010581254789203,0.9062094836371017,0.9233783287144209,0.9338745051748574,0.9705731974939211,0.9760270510168769), b0=doubleArrayOf(1.6944944571208185e-08,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(3.388988914241637e-08,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(1.6944944571208185e-08,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(12699.208415745596,14254.379490245428,11313.708498984759,12500.0,sos=Sos(a1=doubleArrayOf(0.32848784493062516,0.5111660791529813,0.17733074339349109,0.6895165716585276,0.09114717463945445,0.8350589885757965), a2=doubleArrayOf(0.656000255318543,0.6643131072250139,0.7350225079427642,0.7526237525351431,0.8946980509545702,0.9047450752107424), b0=doubleArrayOf(4.079796937465318e-05,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(-8.159593874930636e-05,-2.0,-2.0,2.0,2.0,2.0), b2=doubleArrayOf(4.079796937465318e-05,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(4000.0,4489.8481932374925,3563.594872561357,4000.0,2,Sos(a1=doubleArrayOf(-1.5620018562313807,-1.6020680617209626,-1.5514295488390417,-1.6587733605130148,-1.5779211014645125,-1.7193763872935146), a2=doubleArrayOf(0.8770137966311882,0.8830668098889431,0.9047265072461494,0.9171282371849951,0.9632528802297299,0.9697655086311445), b0=doubleArrayOf(6.450735040335462e-08,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(1.2901470080670925e-07,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(6.450735040335462e-08,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(16000.0,17959.39277294997,14254.379490245428,16000.0,sos=Sos(a1=doubleArrayOf(0.982782646682872,1.1871163009075574,0.8498066536564547,1.3967580458961404,0.837324208994016,1.5838701207898134), a2=doubleArrayOf(0.5651700023975551,0.6111614520796955,0.6434040840366426,0.7378609025916056,0.8507018518753098,0.9043557997025693), b0=doubleArrayOf(0.00013834805505964022,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(-0.00027669611011928044,-2.0,-2.0,2.0,2.0,2.0), b2=doubleArrayOf(0.00013834805505964022,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(5039.684199579493,5656.85424949238,4489.8481932374925,5000.0,1,Sos(a1=doubleArrayOf(-1.3633369519581289,-1.4214643083634073,-1.3380146172088572,-1.495861751008069,-1.3574610576475574,-1.570954880985224), a2=doubleArrayOf(0.8476665728706247,0.8545076730564747,0.8820537169725038,0.896161432848994,0.954298805785257,0.9618028595678236), b0=doubleArrayOf(2.426697477611551e-07,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(4.853394955223102e-07,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(2.426697477611551e-07,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(10079.368399158986,11313.70849898476,8979.696386474985,10000.0,sos=Sos(a1=doubleArrayOf(-0.14222192561841182,-0.29277856105906586,-0.012595844464301289,-0.4348904602476619,0.06824248192606285,-0.5419579256858204), a2=doubleArrayOf(0.7201408449235422,0.7228312366826449,0.7876833682787011,0.7933613741944567,0.9172048817957851,0.9203880227993824), b0=doubleArrayOf(1.170610624091675e-05,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(2.34122124818335e-05,2.0,-2.0,-2.0,2.0,-2.0), b2=doubleArrayOf(1.170610624091675e-05,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(6349.604207872798,7127.189745122714,5656.8542494923795,6300.0,1,Sos(a1=doubleArrayOf(-1.0773395486328345,-1.1602031096288796,-1.0283985417327675,-1.256274714648876,-1.0309072558826313,-1.3467894695867646), a2=doubleArrayOf(0.8121705498843197,0.8193547936396985,0.8548947631581487,0.869820439177322,0.9435278418933211,0.9515891151377003), b0=doubleArrayOf(8.998148499223296e-07,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(1.7996296998446593e-06,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(8.998148499223296e-07,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(12699.208415745596,14254.379490245428,11313.708498984759,12500.0,sos=Sos(a1=doubleArrayOf(0.32848784493062516,0.5111660791529813,0.17733074339349109,0.6895165716585276,0.09114717463945445,0.8350589885757965), a2=doubleArrayOf(0.656000255318543,0.6643131072250139,0.7350225079427642,0.7526237525351431,0.8946980509545702,0.9047450752107424), b0=doubleArrayOf(4.079796937465318e-05,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(-8.159593874930636e-05,-2.0,-2.0,2.0,2.0,2.0), b2=doubleArrayOf(4.079796937465318e-05,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(8000.0,8979.696386474985,7127.189745122714,8000.0,1,Sos(a1=doubleArrayOf(-0.6767266245218572,-0.7912873195095531,-0.5929092846277738,-0.9115385133184579,-0.563294468815372,-1.0151634969766976), a2=doubleArrayOf(0.7697779369631321,0.7761347143350338,0.8231381617779963,0.8364515353139432,0.9309588850819872,0.9382780377204392), b0=doubleArrayOf(3.279097356293735e-06,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(6.55819471258747e-06,2.0,2.0,-2.0,-2.0,-2.0), b2=doubleArrayOf(3.279097356293735e-06,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(16000.0,17959.39277294997,14254.379490245428,16000.0,sos=Sos(a1=doubleArrayOf(0.982782646682872,1.1871163009075574,0.8498066536564547,1.3967580458961404,0.837324208994016,1.5838701207898134), a2=doubleArrayOf(0.5651700023975551,0.6111614520796955,0.6434040840366426,0.7378609025916056,0.8507018518753098,0.9043557997025693), b0=doubleArrayOf(0.00013834805505964022,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(-0.00027669611011928044,-2.0,-2.0,2.0,2.0,2.0), b2=doubleArrayOf(0.00013834805505964022,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(10079.368399158986,11313.70849898476,8979.696386474985,10000.0,0,Sos(a1=doubleArrayOf(-0.14222192561841182,-0.29277856105906586,-0.012595844464301289,-0.4348904602476619,0.06824248192606285,-0.5419579256858204), a2=doubleArrayOf(0.7201408449235422,0.7228312366826449,0.7876833682787011,0.7933613741944567,0.9172048817957851,0.9203880227993824), b0=doubleArrayOf(1.170610624091675e-05,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(2.34122124818335e-05,2.0,-2.0,-2.0,2.0,-2.0), b2=doubleArrayOf(1.170610624091675e-05,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(10079.368399158986,11313.70849898476,8979.696386474985,10000.0,sos=Sos(a1=doubleArrayOf(-0.14222192561841182,-0.29277856105906586,-0.012595844464301289,-0.4348904602476619,0.06824248192606285,-0.5419579256858204), a2=doubleArrayOf(0.7201408449235422,0.7228312366826449,0.7876833682787011,0.7933613741944567,0.9172048817957851,0.9203880227993824), b0=doubleArrayOf(1.170610624091675e-05,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(2.34122124818335e-05,2.0,-2.0,-2.0,2.0,-2.0), b2=doubleArrayOf(1.170610624091675e-05,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(12699.208415745596,14254.379490245428,11313.708498984759,12500.0,0,Sos(a1=doubleArrayOf(0.32848784493062516,0.5111660791529813,0.17733074339349109,0.6895165716585276,0.09114717463945445,0.8350589885757965), a2=doubleArrayOf(0.656000255318543,0.6643131072250139,0.7350225079427642,0.7526237525351431,0.8946980509545702,0.9047450752107424), b0=doubleArrayOf(4.079796937465318e-05,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(-8.159593874930636e-05,-2.0,-2.0,2.0,2.0,2.0), b2=doubleArrayOf(4.079796937465318e-05,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(12699.208415745596,14254.379490245428,11313.708498984759,12500.0,sos=Sos(a1=doubleArrayOf(0.32848784493062516,0.5111660791529813,0.17733074339349109,0.6895165716585276,0.09114717463945445,0.8350589885757965), a2=doubleArrayOf(0.656000255318543,0.6643131072250139,0.7350225079427642,0.7526237525351431,0.8946980509545702,0.9047450752107424), b0=doubleArrayOf(4.079796937465318e-05,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(-8.159593874930636e-05,-2.0,-2.0,2.0,2.0,2.0), b2=doubleArrayOf(4.079796937465318e-05,1.0,1.0,1.0,1.0,1.0)))),
                    Bandpass(16000.0,17959.39277294997,14254.379490245428,16000.0,0,Sos(a1=doubleArrayOf(0.982782646682872,1.1871163009075574,0.8498066536564547,1.3967580458961404,0.837324208994016,1.5838701207898134), a2=doubleArrayOf(0.5651700023975551,0.6111614520796955,0.6434040840366426,0.7378609025916056,0.8507018518753098,0.9043557997025693), b0=doubleArrayOf(0.00013834805505964022,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(-0.00027669611011928044,-2.0,-2.0,2.0,2.0,2.0), b2=doubleArrayOf(0.00013834805505964022,1.0,1.0,1.0,1.0,1.0)),subsamplingFilter=SubsamplingFilter(16000.0,17959.39277294997,14254.379490245428,16000.0,sos=Sos(a1=doubleArrayOf(0.982782646682872,1.1871163009075574,0.8498066536564547,1.3967580458961404,0.837324208994016,1.5838701207898134), a2=doubleArrayOf(0.5651700023975551,0.6111614520796955,0.6434040840366426,0.7378609025916056,0.8507018518753098,0.9043557997025693), b0=doubleArrayOf(0.00013834805505964022,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(-0.00027669611011928044,-2.0,-2.0,2.0,2.0,2.0), b2=doubleArrayOf(0.00013834805505964022,1.0,1.0,1.0,1.0,1.0))))),
                antiAliasing=AntiAliasing(a1=doubleArrayOf(-1.2446640940133593e-16,-1.630640067418199e-16,-1.387778780781446e-16,-2.4980018054066027e-16,-1.9428902930940244e-16,-1.6653345369377353e-16,-2.775557561562892e-16,-1.1102230246251565e-16,-2.220446049250313e-16,-2.2204460492503136e-16), a2=doubleArrayOf(0.001543712508674131,0.014008568735804144,0.03956612989658006,0.07954045177320418,0.13610158059098823,0.21252682194185937,0.31363013821503233,0.4464626921716894,0.6214743340943358,0.8544977810681018), b0=doubleArrayOf(8.555155699386506e-06,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0), b1=doubleArrayOf(1.7110311398773013e-05,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0,2.0), b2=doubleArrayOf(8.555155699386506e-06,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0,1.0),sampleRatio=2),
                aWeighting=DigitalFilterConfiguration(filterDenominator=doubleArrayOf(1.0,-4.01958210258393,6.18944019937876,-4.453264993081893,1.420898591796445,-0.14184390392723248,0.004352219710019002), filterNumerator=doubleArrayOf(0.25574354103517516,-0.5114870820703503,-0.2557435410351751,1.0229741641407009,-0.25574354103517505,-0.5114870820703503,0.25574354103517516)),
                cWeighting=DigitalFilterConfiguration(filterDenominator=doubleArrayOf(1.0,-2.134692386318322,1.279369764820266,-0.14957989013271009,0.004909935845510762), filterNumerator=doubleArrayOf(0.21700689624574065,0.0,-0.4340137924914813,0.0,0.21700689624574065)),
                configuration=GeneralConfiguration(sampleRate=44100))
        }


    @Test
    fun test1000HZ() = runTest {
        val sampleRate = 48000
        val expectedLevel = 94.0
        val peak = (10.0.pow(expectedLevel/20.0)* sqrt(2.0)).toFloat()
        val sum: (Float, Float) -> Float = { x: Float, y: Float -> x + y }

        //var signal = generateSinusoidalFloatSignal(125.0, sampleRate.toDouble(), 5.0) { peak.toFloat() }


        var signal = generateSinusoidalFloatSignal(1000.0, sampleRate.toDouble(), 5.0){peak}
            .zip(generateSinusoidalFloatSignal(1600.0, sampleRate.toDouble(), 5.0){peak}, sum)
            .toFloatArray().zip(
                generateSinusoidalFloatSignal(4000.0, sampleRate.toDouble(), 5.0){peak}, sum
            ).toFloatArray().zip(
                generateSinusoidalFloatSignal(125.0, sampleRate.toDouble(), 5.0){peak}, sum
            ).toFloatArray()

        val sc = SpectrumChannel()
        sc.loadConfiguration(get48000HZ(), true)

        if(signal.size % sc.minimumSamplesLength > 0) {
            // pad with zero
            signal = signal.copyOf(signal.size + (signal.size % sc.minimumSamplesLength))
        }
        val frequencies = sc.getNominalFrequency()
        val thirdOctaves = sc.processSamples(signal)

        assertEquals(expectedLevel, thirdOctaves[frequencies.indexOf(125.0)], 0.1);
        assertEquals(expectedLevel, thirdOctaves[frequencies.indexOf(1000.0)], 0.1);
        assertEquals(expectedLevel, thirdOctaves[frequencies.indexOf(1600.0)], 0.1);
        assertEquals(expectedLevel, thirdOctaves[frequencies.indexOf(4000.0)], 0.1);
    }

    @OptIn(ExperimentalResourceApi::class)
    @Test
    fun testSpeak() = runTest {
        val expectedBA = -33.761
        val expectedBC = -28.763
        // Reference spectrum
        // generated using acoustics.standards.iec_61672_1_2013.time_averaged_sound_level
        val refSpl = doubleArrayOf(
            -65.997, -68.064, -66.276, -43.342, -31.927, -37.280, -47.332, -35.327,
            -42.683, -42.909, -48.506, -49.105, -52.900, -52.152, -52.804, -52.348, -52.313,
            -53.387, -52.532, -53.735, -53.556, -58.004, -66.453
        )

        var r = Resource("src/commonTest/resources/speak_44100Hz_16bitsPCM_10s.raw")

        assertTrue(r.exists())

        val sc = SpectrumChannel()
        sc.loadConfiguration(get44100HZ100TO16000(), true)


        val bArray = r.readBytes()

        val floatArray = FloatArray(bArray.size / 2) { index ->
            val firstByteIndex = index * 2
            val secondByteIndex = firstByteIndex + 1
            val combinedInteger = bArray[firstByteIndex].toInt().and(0xFF) or (bArray[secondByteIndex].toInt() shl 8)
            (combinedInteger / (1L shl 15).toDouble()).toFloat()
        }

        val spectrum = sc.processSamples(floatArray)

        val err = sqrt(refSpl.mapIndexed { i, spl -> (spl - spectrum[i]).pow(2) }.reduce { acc, e -> acc + e } / spectrum.size)

        assertEquals(0.0, err, 0.48)

        assertEquals(expectedBA, sc.processSamplesWeightA(floatArray), 0.01)

        assertEquals(expectedBC, sc.processSamplesWeightC(floatArray), 0.01)

    }
}