package net.bigmir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonMessages {
    private final List<Message> list;

    public JsonMessages(List<Message> sourceList, int fromIndex) {
        this.list = new ArrayList<>();
        for (int i = fromIndex; i < sourceList.size(); i++)
            list.add(sourceList.get(i));
    }

    public List<Message> getList() {
        return Collections.unmodifiableList(list);
    }

    public boolean isMessagePresent(Message m) {
        if (!getList().isEmpty()) {
            for (Message message : getList()) {
                if (message.getId() == m.getId()) {
                    return true;
                }
            }
        }
        return false;
    }
}
