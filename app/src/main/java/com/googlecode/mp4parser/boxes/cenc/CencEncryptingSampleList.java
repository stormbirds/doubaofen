package com.googlecode.mp4parser.boxes.cenc;

import com.googlecode.mp4parser.authoring.Sample;
import com.googlecode.mp4parser.util.CastUtils;
import com.googlecode.mp4parser.util.RangeStartMap;
import com.mp4parser.iso23001.part7.CencSampleAuxiliaryDataFormat;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractList;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;

public class CencEncryptingSampleList extends AbstractList<Sample> {
    List<CencSampleAuxiliaryDataFormat> auxiliaryDataFormats;
    RangeStartMap<Integer, SecretKey> ceks;
    Cipher cipher;
    /* access modifiers changed from: private */
    public final String encryptionAlgo;
    List<Sample> parent;

    public CencEncryptingSampleList(SecretKey secretKey, List<Sample> list, List<CencSampleAuxiliaryDataFormat> list2) {
        this(new RangeStartMap(0, secretKey), list, list2, "cenc");
    }

    public CencEncryptingSampleList(RangeStartMap<Integer, SecretKey> rangeStartMap, List<Sample> list, List<CencSampleAuxiliaryDataFormat> list2, String str) {
        this.ceks = new RangeStartMap<>();
        this.auxiliaryDataFormats = list2;
        this.ceks = rangeStartMap;
        this.encryptionAlgo = str;
        this.parent = list;
        try {
            if ("cenc".equals(str)) {
                this.cipher = Cipher.getInstance("AES/CTR/NoPadding");
            } else if ("cbc1".equals(str)) {
                this.cipher = Cipher.getInstance("AES/CBC/NoPadding");
            } else {
                throw new RuntimeException("Only cenc & cbc1 is supported as encryptionAlgo");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e2) {
            throw new RuntimeException(e2);
        }
    }

    public Sample get(int i) {
        Sample sample = this.parent.get(i);
        if (this.ceks.get(Integer.valueOf(i)) == null) {
            return sample;
        }
        return new EncryptedSampleImpl(this, sample, this.auxiliaryDataFormats.get(i), this.cipher, this.ceks.get(Integer.valueOf(i)), (EncryptedSampleImpl) null);
    }

    /* access modifiers changed from: protected */
    public void initCipher(byte[] bArr, SecretKey secretKey) {
        try {
            byte[] bArr2 = new byte[16];
            System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
            this.cipher.init(1, secretKey, new IvParameterSpec(bArr2));
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e2) {
            throw new RuntimeException(e2);
        }
    }

    public int size() {
        return this.parent.size();
    }

    private class EncryptedSampleImpl implements Sample {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private final SecretKey cek;
        private final CencSampleAuxiliaryDataFormat cencSampleAuxiliaryDataFormat;
        private final Cipher cipher;
        private final Sample clearSample;

        static {
            Class<CencEncryptingSampleList> cls = CencEncryptingSampleList.class;
        }

        /* synthetic */ EncryptedSampleImpl(CencEncryptingSampleList cencEncryptingSampleList, Sample sample, CencSampleAuxiliaryDataFormat cencSampleAuxiliaryDataFormat2, Cipher cipher2, SecretKey secretKey, EncryptedSampleImpl encryptedSampleImpl) {
            this(sample, cencSampleAuxiliaryDataFormat2, cipher2, secretKey);
        }

        private EncryptedSampleImpl(Sample sample, CencSampleAuxiliaryDataFormat cencSampleAuxiliaryDataFormat2, Cipher cipher2, SecretKey secretKey) {
            this.clearSample = sample;
            this.cencSampleAuxiliaryDataFormat = cencSampleAuxiliaryDataFormat2;
            this.cipher = cipher2;
            this.cek = secretKey;
        }

        public void writeTo(WritableByteChannel writableByteChannel) throws IOException {
            ByteBuffer byteBuffer = (ByteBuffer) this.clearSample.asByteBuffer().rewind();
            CencEncryptingSampleList.this.initCipher(this.cencSampleAuxiliaryDataFormat.iv, this.cek);
            try {
                if (this.cencSampleAuxiliaryDataFormat.pairs == null || this.cencSampleAuxiliaryDataFormat.pairs.length <= 0) {
                    byte[] bArr = new byte[byteBuffer.limit()];
                    byteBuffer.get(bArr);
                    if ("cbc1".equals(CencEncryptingSampleList.this.encryptionAlgo)) {
                        int length = (bArr.length / 16) * 16;
                        writableByteChannel.write(ByteBuffer.wrap(this.cipher.doFinal(bArr, 0, length)));
                        writableByteChannel.write(ByteBuffer.wrap(bArr, length, bArr.length - length));
                    } else if ("cenc".equals(CencEncryptingSampleList.this.encryptionAlgo)) {
                        writableByteChannel.write(ByteBuffer.wrap(this.cipher.doFinal(bArr)));
                    }
                } else {
                    byte[] bArr2 = new byte[byteBuffer.limit()];
                    byteBuffer.get(bArr2);
                    int i = 0;
                    for (CencSampleAuxiliaryDataFormat.Pair pair : this.cencSampleAuxiliaryDataFormat.pairs) {
                        int clear = i + pair.clear();
                        if (pair.encrypted() > 0) {
                            this.cipher.update(bArr2, clear, CastUtils.l2i(pair.encrypted()), bArr2, clear);
                            i = (int) (((long) clear) + pair.encrypted());
                        } else {
                            i = clear;
                        }
                    }
                    writableByteChannel.write(ByteBuffer.wrap(bArr2));
                }
                byteBuffer.rewind();
            } catch (IllegalBlockSizeException e) {
                throw new RuntimeException(e);
            } catch (BadPaddingException e2) {
                throw new RuntimeException(e2);
            } catch (ShortBufferException e3) {
                throw new RuntimeException(e3);
            }
        }

        public long getSize() {
            return this.clearSample.getSize();
        }

        public ByteBuffer asByteBuffer() {
            ByteBuffer byteBuffer = (ByteBuffer) this.clearSample.asByteBuffer().rewind();
            ByteBuffer allocate = ByteBuffer.allocate(byteBuffer.limit());
            CencSampleAuxiliaryDataFormat cencSampleAuxiliaryDataFormat2 = this.cencSampleAuxiliaryDataFormat;
            CencEncryptingSampleList.this.initCipher(cencSampleAuxiliaryDataFormat2.iv, this.cek);
            try {
                if (cencSampleAuxiliaryDataFormat2.pairs != null) {
                    for (CencSampleAuxiliaryDataFormat.Pair pair : cencSampleAuxiliaryDataFormat2.pairs) {
                        byte[] bArr = new byte[pair.clear()];
                        byteBuffer.get(bArr);
                        allocate.put(bArr);
                        if (pair.encrypted() > 0) {
                            byte[] bArr2 = new byte[CastUtils.l2i(pair.encrypted())];
                            byteBuffer.get(bArr2);
                            allocate.put(this.cipher.update(bArr2));
                        }
                    }
                } else {
                    byte[] bArr3 = new byte[byteBuffer.limit()];
                    byteBuffer.get(bArr3);
                    if ("cbc1".equals(CencEncryptingSampleList.this.encryptionAlgo)) {
                        int length = (bArr3.length / 16) * 16;
                        allocate.put(this.cipher.doFinal(bArr3, 0, length));
                        allocate.put(bArr3, length, bArr3.length - length);
                    } else if ("cenc".equals(CencEncryptingSampleList.this.encryptionAlgo)) {
                        allocate.put(this.cipher.doFinal(bArr3));
                    }
                }
                byteBuffer.rewind();
                allocate.rewind();
                return allocate;
            } catch (IllegalBlockSizeException e) {
                throw new RuntimeException(e);
            } catch (BadPaddingException e2) {
                throw new RuntimeException(e2);
            }
        }
    }
}
