package de.tudarmstadt.gdi1.project.cipher.substitution;

import de.tudarmstadt.gdi1.project.cipher.substitution.SubstitutionCipher;

/**
 * Created by reckter on 2/17/14.
 */
public abstract class SubstitutionCipherImpl implements SubstitutionCipher {


    /**
     * Encrypt a text according to the encryption method of the cipher
     *
     * @param text the plaintext to encrypt
     * @return the encrypted plaintext (=ciphertext)
     */
    @Override
    public String encrypt(String text) {StringBuilder out = new StringBuilder();
        char[] textArray = text.toCharArray();
        for(int i = 0; i  < textArray.length; i++) {
            out.append(reverseTranslate(textArray[i], i));
        }

        return out.toString();
    }

    /**
     * Decrypt a text according to the decryption method of the cipher
     *
     * @param text the ciphertext to decrypt
     * @return the decrypted ciphertext (=plaintext)
     */
    @Override
    public String decrypt(String text) {
        StringBuilder out = new StringBuilder();
        char[] textArray = text.toCharArray();
        for(int i = 0; i  < textArray.length; i++) {
            out.append(translate(textArray[i], i));
        }

        return out.toString();
    }
}
