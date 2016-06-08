package com.meruvian.toolkit_core;

import java.security.cert.Certificate;
import java.util.Collection;

/**
 * Created by akm on 06/06/16.
 */
public interface Signer<Input, Output> {

    void sign(Input input, Output output) throws Exception;

    Collection<? extends Certificate> getCertificates(Input input) throws Exception;

    boolean isCertificateExist(Input input, Certificate certificate) throws Exception;

}
