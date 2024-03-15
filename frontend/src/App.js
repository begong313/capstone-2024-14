import "./App.css";
import { RecoilRoot } from "recoil";
import GlobalStyle from "./styles/global";
import Router from "./core/router";

// import { Map, MapMarker } from "react-kakao-maps-sdk";

function App() {
  return (
    // <div>
    //   {/* <Map center={{ lat: 33.5563, lng: 126.79581 }} style={{ width: "100%", height: "360px" }}>
    //     <MapMarker position={{ lat: 33.55635, lng: 126.795841 }}>
    //       <div style={{ color: "#000" }}>Hello World!</div>
    //     </MapMarker>
    //   </Map> */}
    // </div>
    <RecoilRoot>
      <GlobalStyle />
      <Router />
    </RecoilRoot>
  );
}

export default App;
