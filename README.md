# Mobile app for verifying cryptographic proofs

A voting application running in a PC browser (JavaScript) provides the voter with some cryptographic
proofs that have to be verified in order to ensure the correctness of the vote to be cast.
These cryptographic proofs are Zero-Knowledge proofs based in the Schnorr Signature protocol.

The voter needs an independent software to verify these proofs, since the verification requires a
lot of computations that cannot be done by human means. A mobile application is proposed to be
implemented to verify such proofs.

This mobile application will receive the proofs information from the PC by scanning **QR codes** presented
in the PC screen or **files** in the user smartphone, execute the required computations to verify
them, and inform the voter of the result of the verification.

The app will read sequentially several QRs or files to gather different input data, present the voter
to input additional data for the verification and finally execute the mathematical operations for
the verification of the cryptographic proofs read from the QRs and inform the voter with the result
of the proof verification.
These mathematical proofs will require to use `BigInteger` capabilities from Java or a specific
library for modular exponentiations in Android.

## Setup

This project is being developed under [Android Studio](https://developer.android.com/sdk/installing/studio.html),
so almost no configuration is required. Nevertheless, it is necessary to make a few tweaks in order to
run the [Robolectric](http://robolectric.org/) tests. This is a reduced version of [this](http://blog.futurice.com/android_unit_testing_in_ides_and_ci_environments)
post:

1. When we first try to run the tests, the run will complain about the JUnit version. The prompt will
show a command from which we need to pick the classpath arg.
2. Move JUnit 4 dependency to the first one in the classpath.
3. Add an absolute entry for the robolectric test classes in the classpath: `Cproofs/cproofs/build/classes/robolectric`
4. Create a custom gradle run configuration which runs the *robolectricClasses* task.
5. Create a JUnit run configuration pointing to the directory: `Cproofs/cproofs/src/test/java`
6. Add the recently created gradle run configuration to the before launch Junit run configuration
 we just created.
7. Tests should be running by now.