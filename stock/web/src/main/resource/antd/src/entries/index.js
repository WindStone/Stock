import './index.html';
import './index.less';
import React from 'react';
import ReactDOM from 'react-dom';
import {Row, Col} from 'antd';
import {
    Select,
    DatePicker,
    Checkbox,
    Radio,
    Input,
    InputNumber,
    Form,
    Button,
    Icon,
    Table,
    Card,
    Switch,
    Pagination,
    message
} from 'antd';
import {TogglePagination} from './TogglePagination'
var Refetch = require('refetch');
const Option = Select.Option;
const CheckboxGroup = Checkbox.Group;
const RangePicker = DatePicker.RangePicker;
const RadioGroup = Radio.Group;
const RadioButton = Radio.Button;
const FormItem = Form.Item;

const filterCheckBoxKeys = [
    {value: "shShare", label: "上证A股", reg: /^SH60/},
    {value: "szShare", label: "深圳A股", reg: /^SZ00/},
    {value: "szGem", label: "深圳创业板", reg: /^SZ300/}
];

const symbolOps = [
    <Option key="gt">{">"}</Option>,
    <Option key="ge">{">="}</Option>,
    <Option key="lt">{"<"}</Option>,
    <Option key="le">{"<="}</Option>
];

const priceTypeOps = [
    <Option key="openingPrice">开盘价格</Option>,
    <Option key="closingPrice">收盘价格</Option>,
    <Option key="highestPrice">最高点价格</Option>,
    <Option key="lowestPrice">最低点价格</Option>
];

const filterCondOps = [
    <Option key="volumeFilter">成交量条件</Option>,
    <Option key="priceFilter">价格条件</Option>
];

const questionStyle = {
    marginLeft: '30%'
};

const resultTableTitles = [{dataIndex: "title", key: "title", title: "比较项"},
    {dataIndex: "oneDayInc", key: "oneDayInc", title: "一日收益率"},
    {dataIndex: "fiveDayInc", key: "fiveDayInc", title: "五日收益率"},
    {dataIndex: "tenDayInc", key: "tenDayInc", title: "十日收益率"}];

const initResultTable = [{key: 1, title: "收益率", oneDayInc: "---", fiveDayInc: "---", tenDayInc: "---"},
    {key: 2, title: "相对沪指收益率", oneDayInc: "---", fiveDayInc: "---", tenDayInc: "---"},
    {key: 3, title: "相对深综收益率", oneDayInc: "---", fiveDayInc: "---", tenDayInc: "---"}];

const lineMarginStyle = {marginTop: 8, marginBottom: 8};

class Filter extends React.Component {

    getDateBefore(date, span) {
        var month = date.getMonth() - span;
        if (month < 0) {
            month += 12;
            date.setYear((date.getYear() - 1) + 1900);
        }
        date.setMonth(month);
        return date;
    }

    constructor(props) {
        super(props);
        this.state = {
            originalData: {"SH600000": "SH600000-浦发银行", "SH600004": "SH600004-机场"},
            optionData: [],
            filterValue: [],
            dateSpan: [this.getDateBefore(new Date(), 3), new Date()],
            dateSpanValue: 3,
            resultData: initResultTable
        };

        var newOptionData = [];
        for (var o in this.state.originalData) {
            newOptionData.push(<Option key={o}>{this.state.originalData[o]}</Option>);
        }
        this.state.optionData = newOptionData;
    }

    componentDidMount() {
        Refetch.jsonp('http://115.28.80.132:8080/stockcode.html').then(function (result) {
            this.setState({originalData: result});
            this.filterChanged(this.state.filterValue).bind(this);
        }.bind(this));
    }

    render() {
        return (
            <div style={{margin: 15}}>
                <Card title="过滤条件" bordered={true}>
                    <Form>
                        <Row type="flex" justify="start" align="middle" style={lineMarginStyle}>
                            <Col span={2}>
                            <span style={{float: 'right', marginRight: '10%'}}>
                                股票过滤：
                            </span>
                            </Col>
                            <Col span={15}>
                                <Select multiple style={{width: '100%'}}>
                                    {this.state.optionData}
                                </Select>
                            </Col>
                            <Col span={5}>
                            <span style={{float: 'right'}}>
                            <CheckboxGroup options={filterCheckBoxKeys}
                                           value={this.state.filterValue}
                                           onChange={this.filterChanged.bind(this)}/>
                            </span>
                            </Col>
                            <Col span={1}>
                                <Icon type="question-circle-o" style={questionStyle}/>
                            </Col>
                        </Row>
                        <Row type="flex" justify="start" align="middle" style={lineMarginStyle}>
                            <Col span={2}>
                            <span style={{float: 'right', marginRight: '10%'}}>
                                时间过滤：
                            </span>
                            </Col>
                            <Col span={15}>
                                <RangePicker value={this.state.dateSpan} style={{width: '100%'}}
                                             onChange={this.dataSpanChanged.bind(this)}/>
                            </Col>
                            <Col span={5}>
                                <RadioGroup value={this.state.dateSpanValue} onChange={(e) => {
                                    this.setState({
                                        dateSpanValue: e.target.value,
                                        dateSpan: [this.getDateBefore(new Date(), e.target.value), new Date()]
                                    });
                                }} style={{float: 'right'}}>
                                    <RadioButton value={1}>1个月</RadioButton>
                                    <RadioButton value={3}>3个月</RadioButton>
                                    <RadioButton value={6}>6个月</RadioButton>
                                    <RadioButton value={12}>12个月</RadioButton>
                                </RadioGroup>
                            </Col>
                            <Col span={1}>
                                <Icon type="question-circle-o" style={questionStyle}/>
                            </Col>
                        </Row>
                        <Row type="flex" justify="start" align="middle" style={lineMarginStyle}>
                            <Col span={2}>
                                <span style={{float: 'right', marginRight: '10%'}}>成交量条件：</span>
                            </Col>
                            <Col span={20}>
                                <span>成交量</span>
                                <Select name="volumeSymbol" style={{width: 54, marginLeft: 5, marginRight: 5}}>
                                    {symbolOps}
                                </Select>
                                <span>前</span>
                                <InputNumber name="volumeWindow" style={{width: 54, marginLeft: 5, marginRight: 5}}
                                             min={1} step={1} defaultValue={10}/>
                                <span>日平均成交量</span>
                                <InputNumber name="volumePercent" style={{width: 54, marginLeft: 5, marginRight: 0}}
                                             min={0} step={5} defaultValue={0}/>
                                <span>%</span>
                            </Col>
                            <Col span={1}>
                                <Icon type="question-circle-o" style={questionStyle}/>
                            </Col>
                        </Row>
                        <Row type="flex" justify="start" align="middle" style={lineMarginStyle}>
                            <Col span={2}>
                                <span style={{float: 'right', marginRight: '10%'}}>价格条件：</span>
                            </Col>
                            <Col span={20}>
                                <span>股票</span>
                                <Select name="priceType" style={{width: 120, marginLeft: 5, marginRight: 5}}>
                                    {priceTypeOps}
                                </Select>
                                <Select name="priceSymbol" style={{width: 54, marginLeft: 5, marginRight: 5}}>
                                    {symbolOps}
                                </Select>
                                <span>最近</span>
                                <InputNumber name="priceWindow" style={{width: 54, marginLeft: 5, marginRight: 5}}
                                             min={1} step={1} defaultValue={10}/>
                                <span>日</span>
                                <Select name="priceWindowType" style={{width: 120, marginLeft: 5, marginRight: 5}}>
                                    {priceTypeOps}
                                </Select>
                                <span>最大值的</span>
                                <InputNumber name="pricePercent" style={{width: 54, marginLeft: 5, marginRight: 0}}
                                             min={0} step={5} defaultValue={0}/>
                                <span>%</span>
                            </Col>
                            <Col span={1}>
                                <Icon type="question-circle-o" style={questionStyle}/>
                            </Col>
                        </Row>
                        <Row style={{marginTop: 20, marginBottom: 0}}>
                            <Col span={3} offset={2}>
                                <Button type="primary" style={{width: '45%', marginRight: '10%'}}>处理</Button>
                                <Button style={{width: '45%'}}>重置</Button>
                            </Col>
                            <Col span={16} offset={1}>
                                <Select style={{width: '89.69%'}}>
                                    {filterCondOps}
                                </Select>
                                <Button style={{width: '8.44%', marginLeft: '1.87%'}}>增加</Button>
                            </Col>
                        </Row>
                    </Form>
                </Card>
                <Card title="处理结果" style={{marginTop: 15, marginBottom: 15}}>
                    <Row>
                        <Col span={20} offset={2}>
                            <Table bordered columns={resultTableTitles} dataSource={this.state.resultData}>
                            </Table>
                        </Col>
                    </Row>
                    <Row>
                        <TogglePagination/>
                    </Row>
                </Card>
            </div>
        );
    }

    filterChanged(value) {
        var newOptionData = [];
        var arrReg = [];
        filterCheckBoxKeys.map(function (data) {
            if (value.indexOf(data.value) != -1) {
                arrReg.push(data.reg);
            }
        });
        for (var o in this.state.originalData) {
            var match = false;
            arrReg.map(function (reg) {
                if (o.match(reg))   match = true;
            });
            if (!match) newOptionData.push(<Option key={o}>{this.state.originalData[o]}</Option>);
        }
        this.setState({optionData: newOptionData, filterValue: value});
    }

    dataSpanChanged(value, dateString) {
        var date = new Date();
        if (value[1].getDate() == date.getDate() && value[0].getDate() == date.getDate()) {
            var month = value[1].getYear() * 12 + value[1].getMonth() - value[0].getYear() * 12 - value[0].getMonth();
            this.setState({dateSpan: value, dateSpanValue: month});
        } else {
            this.setState({dateSpan: value, dateSpanValue: 0});
        }
    }
}

ReactDOM.render(
    <Filter />
    , document.getElementById('root'));
