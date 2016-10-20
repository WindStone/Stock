'use strict';

import {Component} from "react";
import {
    Form,
    FormControl,
    FormSubmit,
    Modal,
    Input,
    Select,
    CheckboxGroup,
    Datepicker,
    RadioGroup,
    Table,
    Refetch,
    Pagination
} from "rctui";
import {refetch} from "refetch";

function _interopRequireDefault(obj) {
    return obj && obj.__esModule ? obj : {default: obj};
}
var _refetch = require('refetch');
var _refetch2 = _interopRequireDefault(_refetch);

class Demo extends Component {

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
        var _this = this;

        this.state = {
            original_data: '',
            data: {"Loading": "Loading"},
            startDate: this.getDateBefore(new Date(), 3),
            endDate: new Date(),
            statData: [{title: "收益率", oneDayInc: "", fiveDayInc: "", tenDayInc: ""},
                {title: "相对沪指收益率", oneDayInc: "", fiveDayInc: "", tenDayInc: ""},
                {title: "相对深综收益率", oneDayInc: "", fiveDayInc: "", tenDayInc: ""}],
            buttonName: '获取数据',
            debugResult: []
        }

        this.filterCheckboxKey = [{id: "sh_share", text: "上证A股", reg: /^SH60/},
            {id: "sz_share", text: "深圳A股", reg: /^SZ00/},
            {id: "sz_gem", text: "深圳创业板", reg: /^SZ300/}];
        this.timeSpanKey = [{id: "3-month", text: "最近三个月", span: 3},
            {id: "6-month", text: "最近六个月", span: 6},
            {id: "12-month", text: "最近一年", span: 12}];
        this.opSymbolKey = [{id: "gt", text: ">"}, {id: "ge", text: ">="},
            {id: "lt", text: "<"}, {id: "le", text: "<="}];
        this.priceTypeKey = [{id: "openingPrice", text: "开盘价格"}, {id: "closingPrice", text: "收盘价格"},
            {id: "highestPrice", text: "最高点价格"}, {id: "lowestPrice", text: "最低点价格"}];

        let request = _refetch2.default['jsonp']('stockcode.html');

        request.then((data) => {
            _this.setState({original_data: data, data: data})
        })
            .catch((err) => {
                console.warn(err);
                setState({original_data: new Error(), data: new Error()});
            });
    }

    processRes(res) {
        console.log(res);
        this.setState({buttonName: '获取数据'});
        this.setState({statData: res.result, debugResult: res.debug});
    }

    submitAction(data) {
        this.setState({buttonName: '数据处理中'});
        Refetch.jsonp("statistics/index.html?request="
            + JSON.stringify(data)).then(this.processRes.bind(this))
    }

    render() {
        return (
            <div>
                <Form onSubmit={this.submitAction.bind(this)}>
                    <FormControl>
                        <Select name="specificFilterCode"
                                grid={{width: 1 / 2}}
                                mult={true}
                                placeholder="过滤的股票"
                                filterAble={true}
                                data={this.state.data}/>
                        <CheckboxGroup name="filterGroup" className="rct-grid"
                                       onChange={this.filterChanged.bind(this)}
                                       data={this.filterCheckboxKey}/>
                    </FormControl>
                    <FormControl>
                        <Datepicker type="date" name="startDate" format="yyyy-MM-dd"
                                    min="2014-01-01" max={new Date()}
                                    value={this.state.startDate}/>
                        <span>-</span>
                        <Datepicker type="date" name="endDate" format="yyyy-MM-dd"
                                    min="2014-01-01" max={new Date()}
                                    value={this.state.endDate}/>
                        <RadioGroup name="specificInterval" className="rct-grid"
                                    onChange={this.timeSpanChanged.bind(this)}
                                    data={this.timeSpanKey}/>
                    </FormControl>
                    <FormControl>
                        <span>成交量</span>
                        <Select name="volumeSymbol" style={{width: 100}} data={this.opSymbolKey}/>
                        <span>前</span>
                        <Input name="volumeWindow" style={{width: 54}} type="integer"/>
                        <span>日平均成交量</span>
                        <Input name="volumePercent" style={{width: 54}} type="integer"/>
                        <span>%</span>
                    </FormControl>
                    <FormControl>
                        <span>股票</span>
                        <Select name="priceType" style={{width: 120}} data={this.priceTypeKey}/>
                        <Select name="priceSymbol" style={{width: 100}} data={this.opSymbolKey}/>
                        <span>最近</span>
                        <Input name="priceWindow" style={{width: 54}} type="integer"/>
                        <span>日</span>
                        <Select name="priceWindowType" style={{width: 120}} data={this.priceTypeKey}/>
                        <span>最大值的</span>
                        <Input name="pricePercent" style={{width: 54}} type="integer"/>
                        <span>%</span>
                    </FormControl>
                    <Table bordered={true} striped={true}
                           headers={[{name: "title", sortAble: true, header: "比较项"},
                               {name: "oneDayInc", sortAble: true, header: "一日收益率"},
                               {name: "fiveDayInc", sortAble: true, header: "五日收益率"},
                               {name: "tenDayInc", sortAble: true, header: "十日收益率"}]}
                           data={this.state.statData}/>
                    <FormSubmit>{this.state.buttonName}</FormSubmit>
                    <Table bordered={true} striped={true}
                           headers={[{name: "date", sortAble: true, header: "日期"},
                               {name: "stockName", sortAble: true, header: "股票名称"},
                               {name: "stockCode", sortAble: true, header: "股票代码"},
                               {name: "type", sortAble: true, header: "涨幅类型"},
                               {name: "raisingRate", sortAble: true, header: "涨幅"},
                               {name: "shRaisingRate", sortAble: true, header: "上证指数涨幅"},
                               {name: "szRaisingRate", sortAble: true, header: "深圳综指涨幅"}]}
                           data={this.state.debugResult}
                           pagination={<Pagination size={20} jumper={true} total={this.state.data.length}/>}/>
                </Form>
            </div>
        );
    }

    filterChanged(value) {
        var new_data = {};
        var arr_reg = [];
        this.filterCheckboxKey.map(function (data) {
            if (value.indexOf(data.id) != -1) {
                arr_reg.push(data.reg);
            }
        });

        var original_data = this.state.original_data;
        for (var o in original_data) {
            var match = false;
            arr_reg.map(function (reg) {
                if (o.match(reg))   match = true;
            });
            if (!match) new_data[o] = original_data[o];
        }
        this.setState({data: new_data});
    }

    timeSpanChanged(value) {
        var _this = this;
        var span = 3;
        _this.timeSpanKey.map(function (key) {
            if (key.id === value) {
                span = key.span;
            }
        });
        var startDate = this.getDateBefore(new Date(), span);
        this.setState({startDate: startDate, endDate: new Date()});
    }

}

ReactDOM.render(
    <Demo />
    , document.getElementById('demo'));

