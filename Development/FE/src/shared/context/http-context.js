import { createContext } from "react";

export const HttpContext = createContext({
  isLoading: false, 
  error: null, 
  sendRequest: () => {}, 
  clearError: () => {},
});
