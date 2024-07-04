import React from 'react';
import { render, screen } from '@testing-library/react';
import '@testing-library/jest-dom';
import Banner from '../Home/Banner';
import styles from '../Home/style.module.css';

describe('Banner Component', () => {
  it('renders the banner image with correct alt text', () => {
    render(<Banner />);

    const bannerImage = screen.getByAltText('banner');
    expect(bannerImage).toBeInTheDocument();
    expect(bannerImage).toHaveClass(styles.img);
  });

  it('renders the title and description with correct text and styles', () => {
    render(<Banner />);

    const title = screen.getByText('南方科技大学活动中心');
    const description = screen.getByText('SUSTech Entertainment Center');

    expect(title).toBeInTheDocument();
    expect(title).toHaveClass(styles.title);

    expect(description).toBeInTheDocument();
    expect(description).toHaveClass(styles.description);
  });

});
