import { combineReducers } from "redux";
import { persistReducer } from "redux-persist";
import storage from "redux-persist/lib/storage";

// local reducers
import userReducer from "../features/userSlice";
import moviesReducer from "../features/movieSlice";
import pictureReducer from "../features/pictureSlice";

const persistConfig = {
  key: "root",
  storage,
  whitelist: ["user", "movies", "picture"],
};

const rootReducer = combineReducers({
  user: userReducer,
  movies: moviesReducer,
  picture: pictureReducer,
});

export default persistReducer(persistConfig, rootReducer);
