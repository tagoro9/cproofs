# Mobile app for verifying cryptographic proofs

A voting application running in a PC browser (JavaScript) provides the voter with some cryptographic
proofs that have to be verified in order to ensure the correctness of the vote to be cast.
These cryptographic proofs are Zero-Knowledge proofs based in the Schnorr Signature protocol.

The voter needs an independent software to verify these proofs, since the verification requires a
lot of computations that cannot be done by human means. A mobile application is proposed to be
implemented to verify such proofs.

This mobile application will receive the proofs information from the PC by scanning **QR codes** presented
in the PC screen or **files** in the user smartphone, execute the required computations to verify
them, and inform the voter of the result of the verification. Given:

The app will read sequentially several QRs or files to gather different input data, present the voter
to input additional data for the verification and finally execute the mathematical operations for
the verification of the cryptographic proofs read from the QRs and inform the voter with the result
of the proof verification.
These mathematical proofs will require to use `BigInteger` capabilities from Java or a specific
library for modular exponentiations in Android.