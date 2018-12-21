import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { fetchProduct, productSearch } from '../../actions/index';

import { Title } from '../Styled';
import Github from './Github';

const Header = ({ fetchProduct, productId, productSearch }) => (
  <div>
    <div onClick={() => fetchProduct(productId)}>Get Products</div>
    <div onClick={() => productSearch('garlic')}>Search Product</div>
    <Title>redux-react-starter</Title>
    <Github />
  </div>
);

Header.propTypes = {
  fetchProduct: PropTypes.func.isRequired,
  productSearch: PropTypes.func.isRequired,
  productId: PropTypes.number.isRequired,
};


const mapStateToProps = () => ({ productId: 45023303 });
const mapDispatchToProps = dispatch => ({
  fetchProduct: productId => dispatch(fetchProduct(productId)),
  productSearch: searchString => dispatch(productSearch(searchString)),
});

export default connect(mapStateToProps, mapDispatchToProps)(Header);
