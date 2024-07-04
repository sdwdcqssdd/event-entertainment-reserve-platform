import React from 'react';
import { createRoot } from 'react-dom/client';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { ThemeProvider } from './ThemeContext';  // 引入 ThemeProvider

/*ReactDOM.render(
    <ThemeProvider>  // 使用 ThemeProvider 包裹 App 组件
        <App />
    </ThemeProvider>,
    document.getElementById('root')
);*/

const root = createRoot(document.getElementById('root'));
root.render(
    <ThemeProvider>
    <App />
    </ThemeProvider>,);

// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
//   <React.StrictMode>
//     <App />
//   </React.StrictMode>
// );

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
