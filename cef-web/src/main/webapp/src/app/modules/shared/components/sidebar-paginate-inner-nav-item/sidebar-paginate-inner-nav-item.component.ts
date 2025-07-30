/*
 MIT License

Copyright (c) 2025 EPA CAERS Project Team

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
import { Component, OnInit, Input } from '@angular/core';
import { SideNavItem } from 'src/app/shared/models/side-nav-item';
import { SharedService } from 'src/app/core/services/shared.service';

@Component({
  selector: 'app-sidebar-paginate-inner-nav-item',
  templateUrl: './sidebar-paginate-inner-nav-item.component.html',
  styleUrls: ['./sidebar-paginate-inner-nav-item.component.scss']
})
export class SidebarPaginateInnerNavItemComponent implements OnInit {
  @Input() group: number;
  @Input() navItems: SideNavItem[];
  @Input() baseUrl = '';
  @Input() paginate: boolean;
  paginatedGroupKey = '';
  collapsedKey = ''
  groupMin: number = 0;
  groupRange = '';

  constructor(private sharedService: SharedService) { }

  ngOnInit() {
    this.groupMin = this.group - 24;
    this.groupRange = this.groupMin + '-' + this.group;
    this.paginatedGroupKey = this.groupRange + '_';
    if (this.baseUrl) {
      this.collapsedKey = this.paginatedGroupKey + this.baseUrl;
      const cachedCollapse = this.sharedService.getSideNavCollapsedMapEntry(this.collapsedKey);
      if (cachedCollapse == undefined) {
        this.sharedService.setSideNavCollapsedMapEntry(this.collapsedKey, false);
      }
    }
  }

  getCollapsedMapEntry(): boolean {
    return this.sharedService.getSideNavCollapsedMapEntry(this.collapsedKey);
  }

  toggleCollapsedMapEntry() {
    this.sharedService.setSideNavCollapsedMapEntry(
      this.collapsedKey,
      !this.sharedService.getSideNavCollapsedMapEntry(this.collapsedKey)
    );
  }
}
