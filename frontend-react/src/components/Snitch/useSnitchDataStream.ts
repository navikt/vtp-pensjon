import { useState, useEffect } from "react";
import { Stomp, StompSubscription } from "@stomp/stompjs";
import { RequestResponse } from "./types";

function sortByTimestamp(
  requestResponses: RequestResponse[]
): RequestResponse[] {
  return requestResponses.sort((r1, r2) =>
    r1.timestamp > r2.timestamp ? -1 : 1
  );
}

export default function useSnitchDataStream(): RequestResponse[] {
  const [requests, setRequests] = useState<RequestResponse[]>([]);

  useEffect(() => {
    console.log("Opening Stomp/snitch connection.");
    // Start subscription before fetching existing data. A reverse order may lose data between existing data
    // is fetched and a subscription is started
    const websocketUrl = `${window.location.protocol.replace("http", "ws")}//${
      window.location.hostname
    }:${window.location.port}/api/ws`;
    const client = Stomp.client(websocketUrl);
    client.debug = () => {}; // do nothing

    let subscription: StompSubscription;
    client.onConnect = () => {
      subscription = client.subscribe("/topic/snitch", (message) => {
        setRequests((state) => {
          return sortByTimestamp([JSON.parse(message.body), ...state]);
        });
      });
    };
    client.onStompError = (error) => {
      console.error("Stomp error", error);
    };

    client.activate();

    fetch("/data/requestResponses")
      .then((response) => response.json())
      .then((result) =>
        setRequests((state) => {
          return sortByTimestamp([
            ...result._embedded.requestResponses,
            ...state,
          ]);
        })
      );

    return () => {
      console.log("Closing Stomp/snitch connection.");
      subscription && subscription.unsubscribe();
      client.deactivate();
    };
  }, []);

  return requests;
}
