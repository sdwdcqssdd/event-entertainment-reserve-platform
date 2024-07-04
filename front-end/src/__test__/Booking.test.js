import React from 'react';
import { render, screen, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';
import Booking from '../pages/Booking';

const mockEvents = [
    { id: 1, name: 'Yoga Class', date: '2024-04-20', time: '10:00 AM' },
    { id: 2, name: 'Cooking Workshop', date: '2024-04-22', time: '02:00 PM' },
    { id: 3, name: 'Online Webinar', date: '2024-04-25', time: '05:00 PM' }
];

describe('Booking Component', () => {
    beforeEach(() => {
        // 清除 localStorage
        localStorage.clear();
    });

    it('renders available events', () => {
        render(<Booking />);

        mockEvents.forEach(event => {

          mockEvents.forEach(event => {
            expect(screen.getByText(event.name)).toBeInTheDocument();
           
        });
       
        });
    });
    
});
