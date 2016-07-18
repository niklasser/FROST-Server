/*
 * Copyright (C) 2016 Fraunhofer Institut IOSB, Fraunhoferstr. 1, D 76131
 * Karlsruhe, Germany.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.fraunhofer.iosb.ilt.sta.query;

import de.fraunhofer.iosb.ilt.sta.Constants;
import de.fraunhofer.iosb.ilt.sta.path.Property;
import de.fraunhofer.iosb.ilt.sta.query.expression.Expression;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jab
 */
public class Query {

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Query.class);
    private Optional<Integer> top;
    private Optional<Integer> skip;
    private boolean count;
    private List<Property> select;
    private Expression filter;
    private List<Expand> expand;
    private List<OrderBy> orderBy;

    public Query() {
        this.top = Optional.empty();
        this.skip = Optional.empty();
        this.count = Constants.DEFAULT_COUNT;
        this.orderBy = new ArrayList<>();
        this.expand = new ArrayList<>();
        this.select = new ArrayList<>();
    }

    public Optional<Integer> getTop() {
        return top;
    }

    public Optional<Integer> getSkip() {
        return skip;
    }

    public boolean isCount() {
        return count;
    }

    public void setSelect(List<Property> select) {
        this.select = select;
    }

    public List<Property> getSelect() {
        return select;
    }

    public Expression getFilter() {
        return filter;
    }

    public List<Expand> getExpand() {
        return expand;
    }

    public List<OrderBy> getOrderBy() {
        return orderBy;
    }

    public void setTop(int top) {
        this.top = Optional.of(top);
    }

    public void setSkip(int skip) {
        this.skip = Optional.of(skip);
    }

    public void setCount(boolean count) {
        this.count = count;
    }

    public void setFilter(Expression filter) {
        this.filter = filter;
    }

    public void setExpand(List<Expand> expand) {
        this.expand = expand;
    }

    public void addExpand(Expand expand) {
        this.expand.add(expand);
    }

    public void setOrderBy(List<OrderBy> orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.top);
        hash = 73 * hash + Objects.hashCode(this.skip);
        hash = 73 * hash + (this.count ? 1 : 0);
        hash = 73 * hash + Objects.hashCode(this.select);
        hash = 73 * hash + Objects.hashCode(this.filter);
        hash = 73 * hash + Objects.hashCode(this.expand);
        hash = 73 * hash + Objects.hashCode(this.orderBy);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Query other = (Query) obj;
        if (this.count != other.count) {
            return false;
        }
        if (!Objects.equals(this.top, other.top)) {
            return false;
        }
        if (!Objects.equals(this.skip, other.skip)) {
            return false;
        }
        if (!Objects.equals(this.select, other.select)) {
            return false;
        }
        if (!Objects.equals(this.filter, other.filter)) {
            return false;
        }
        if (!Objects.equals(this.expand, other.expand)) {
            return false;
        }
        if (!Objects.equals(this.orderBy, other.orderBy)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean inExpand) {
        char separator = inExpand ? ';' : '&';

        StringBuilder sb = new StringBuilder();
        if (top.isPresent()) {
            sb.append(separator).append("$top=").append(top.get());
        }
        if (skip.isPresent()) {
            sb.append(separator).append("$skip=").append(skip.get());
        }
        if (!select.isEmpty()) {
            sb.append(separator).append("$select=");
            boolean firstDone = false;
            for (Property property : select) {
                if (firstDone) {
                    sb.append(",");
                } else {
                    firstDone = true;
                }
                try {
                    sb.append(URLEncoder.encode(property.getName(), "UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                    LOGGER.error("UTF-8 not supported?!", ex);
                }
            }
        }
        if (filter != null) {
            sb.append(separator).append("$filter=");
            sb.append(filter.toUrl());
        }
        if (!expand.isEmpty()) {
            sb.append(separator).append("$expand=");
            boolean firstDone = false;
            for (Expand e : expand) {
                if (firstDone) {
                    sb.append(",");
                } else {
                    firstDone = true;
                }
                sb.append(e.toString());
            }
        }
        if (!orderBy.isEmpty()) {
            sb.append(separator).append("$orderby=");
            boolean firstDone = false;
            for (OrderBy ob : orderBy) {
                if (firstDone) {
                    sb.append(",");
                } else {
                    firstDone = true;
                }
                sb.append(ob.toString());
            }
        }
        sb.append(separator).append("$count=").append(count);
        if (sb.length() > 0) {
            return sb.substring(1);
        }
        return "";
    }

}