import { createStore } from "@reduxjs/toolkit";
import { persistStore } from "redux-persist";

// reducer
import rootReducer from "./reducer";

export const store = createStore(rootReducer);

export const persistor = persistStore(store);
