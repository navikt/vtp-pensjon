declare var require: any;

import { Pipe, PipeTransform } from '@angular/core';

const format = require('xml-formatter');

@Pipe({
  name: 'xmlformatter'
})
export class XmlformatterPipe implements PipeTransform {

  transform(value: string | null | undefined): string | null {
    if (value != null) {
      return format(value);
    } else {
      return null;
    }
  }
}
