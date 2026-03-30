package pl.tkowalcz.tjahzi.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

import java.nio.charset.StandardCharsets;

import static pl.tkowalcz.tjahzi.protobuf.Protobuf.writeUnsignedVarint;

public class StringSerializer {

    public static void serialize(
            CharSequence logLine,
            ByteBuf target
    ) {
        int utf8Bytes = ByteBufUtil.utf8Bytes(logLine);
        writeUnsignedVarint(utf8Bytes, target);
        target.writeCharSequence(logLine, StandardCharsets.UTF_8);
    }

    public static void serialize(
            ByteBuf logLine,
            ByteBuf target
    ) {
        int stringLength = logLine.readIntLE();
        writeUnsignedVarint(stringLength, target);
        target.writeBytes(logLine, stringLength);
    }
}
