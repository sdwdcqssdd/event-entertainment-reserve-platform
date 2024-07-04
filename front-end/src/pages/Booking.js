
import React, { useState, useEffect } from 'react';

const mockEvents = [
    { id: 1, name: 'Yoga Class', date: '2024-04-20', time: '10:00 AM' },
    { id: 2, name: 'Cooking Workshop', date: '2024-04-22', time: '02:00 PM' },
    { id: 3, name: 'Online Webinar', date: '2024-04-25', time: '05:00 PM' }
];

function Booking() {
    // Assuming booked events are stored in local storage or fetched from an API
    const [bookedEvents, setBookedEvents] = useState([]);

    useEffect(() => {
        // Fetch booked events from local storage or API
        const storedEvents = localStorage.getItem('bookedEvents');
        if (storedEvents) {
            setBookedEvents(JSON.parse(storedEvents));
        }
    }, []);

    const handleBookEvent = (event) => {
        const newBookedEvents = [...bookedEvents, event];
        setBookedEvents(newBookedEvents);
        localStorage.setItem('bookedEvents', JSON.stringify(newBookedEvents));
    };

    return (
        <div>
            <h2>已预约活动</h2>
            <div>
                {bookedEvents.map((event) => (
                    <div key={event.id}>
                        <h3>{event.name}</h3>
                        <p>Date: {event.date}</p>
                        <p>Time: {event.time}</p>
                    </div>
                ))}
            </div>
            <h2>可预约活动</h2>
            <div>
                {mockEvents.map((event) => (
                    <div key={event.id}>
                        <h3>{event.name}</h3>
                        <p>Date: {event.date}</p>
                        <p>Time: {event.time}</p>
                        
                        <button onClick={() => handleBookEvent(event)}>预约</button>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default Booking;
