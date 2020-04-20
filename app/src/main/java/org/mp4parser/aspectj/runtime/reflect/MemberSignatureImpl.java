package org.mp4parser.aspectj.runtime.reflect;

import org.mp4parser.aspectj.lang.reflect.MemberSignature;

abstract class MemberSignatureImpl extends SignatureImpl implements MemberSignature {
    MemberSignatureImpl(int i, String str, Class cls) {
        super(i, str, cls);
    }

    public MemberSignatureImpl(String str) {
        super(str);
    }
}
