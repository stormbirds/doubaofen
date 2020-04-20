package com.googlecode.mp4parser.authoring.builder;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ByteBufferHelper {
    public static List<ByteBuffer> mergeAdjacentBuffers(List<ByteBuffer> list) {
        ArrayList arrayList = new ArrayList(list.size());
        for (ByteBuffer next : list) {
            int size = arrayList.size() - 1;
            if (size >= 0 && next.hasArray() && ((ByteBuffer) arrayList.get(size)).hasArray() && next.array() == ((ByteBuffer) arrayList.get(size)).array() && ((ByteBuffer) arrayList.get(size)).arrayOffset() + ((ByteBuffer) arrayList.get(size)).limit() == next.arrayOffset()) {
                ByteBuffer byteBuffer = (ByteBuffer) arrayList.remove(size);
                arrayList.add(ByteBuffer.wrap(next.array(), byteBuffer.arrayOffset(), byteBuffer.limit() + next.limit()).slice());
            } else if (size < 0 || !(next instanceof MappedByteBuffer) || !(arrayList.get(size) instanceof MappedByteBuffer) || ((ByteBuffer) arrayList.get(size)).limit() != ((ByteBuffer) arrayList.get(size)).capacity() - next.capacity()) {
                next.reset();
                arrayList.add(next);
            } else {
                ByteBuffer byteBuffer2 = (ByteBuffer) arrayList.get(size);
                byteBuffer2.limit(next.limit() + byteBuffer2.limit());
            }
        }
        return arrayList;
    }
}
