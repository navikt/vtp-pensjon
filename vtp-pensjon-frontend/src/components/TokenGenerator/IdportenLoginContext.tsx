import {createContext, useState} from "react";

const DataContextDefaultValue = {
    code: "",
    setCode: (value: string) => {},
};

export const DataContext = createContext(DataContextDefaultValue);

interface DataContextProviderProps {
    children?: React.ReactNode;
}

const DataContextProvider = (props: DataContextProviderProps) => {

    const [code, setCode] = useState("Default");

    return (
        <DataContext.Provider
            value={{
                code,
                setCode
            }}>
            {props.children}
        </DataContext.Provider>
    );

}

export default DataContextProvider
