import { createStore } from '@reduxjs/toolkit';
import userReducer from '../reducer/userReducer';

export default createStore(userReducer);
