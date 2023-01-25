package com.example.diabetus.screens.common;

import java.util.List;

public interface ObservableView<ListenerType> extends ViewMvc {

    void registerListener(ListenerType listener);

    void unregisterListener(ListenerType listener);
}
